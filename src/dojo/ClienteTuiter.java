package dojo;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class ClienteTuiter {
	
	private Servidor servidor;
	private Usuario meuUsuario;
	
	private static String INIT_CHAR = "$ ";

	private static String COMANDOS = "Comandos:" + System.lineSeparator() +
			 "\t /ajuda : Mostra comandos disponiveis" + System.lineSeparator() +
			 "\t /enviaDM <nomeUsuario> : Envia mensagem direta para usuario" + System.lineSeparator() +
			 "\t /login <nomeUsuario> : Faz login com um nome de usuario" + System.lineSeparator() +
			 "\t /perfil <nomeUsuario> : Visualiza postagens de um usuario" + System.lineSeparator() +
			 "\t /post <mensagem> : Postar mensagem" + System.lineSeparator() +
			 "\t /sair : Sair do programa" + System.lineSeparator() +
			 "\t /seguindo : Lista usu√°rios seguidos" + System.lineSeparator() +
			 "\t /seguir <nomeUsuario> : Segue outro usuario" + System.lineSeparator() +
			 "\t /tl : Visualiza linha do tempo" + System.lineSeparator() +
			 "\t /usuarios : Lista usu√°rios do sistema" + System.lineSeparator() +
			 "\t /verDM : Visualiza mensagens diretas recebidas" + System.lineSeparator();

	public ClienteTuiter(String hostRegistry) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(hostRegistry);
		this.servidor = (Servidor) registry.lookup(ServidorTuiter.NOME_SERVIDOR);
	}
	
	public Usuario login(String nomeUsuario) throws RemoteException {
		meuUsuario = servidor.conecta(nomeUsuario);
		return meuUsuario;
	}

	public void segueUsuario(String nomeUsuario) throws RemoteException {
		Usuario outroUsuario = servidor.getUsuarios().get(nomeUsuario);
		if (meuUsuario != null & outroUsuario != null) {
			meuUsuario.seguirOutroUsuario(outroUsuario);
		}
	}
	
	public String listaUsuarios() throws RemoteException {
		StringBuilder strBuilder = new StringBuilder();
		for (String nomeUsuario : servidor.getUsuarios().keySet()) {
			strBuilder.append(nomeUsuario + System.lineSeparator());
		}
		return strBuilder.toString();
	}
	
	public String listaSeguidos() throws RemoteException {
		StringBuilder strBuilder = new StringBuilder();
		if (meuUsuario != null) {
			for (Usuario usuario : meuUsuario.getSeguidos()) {
				strBuilder.append(usuario.getNome() + System.lineSeparator());
			}
		}
		return(strBuilder.toString());
	}
	
	public void criaPostagem(String mensagem) throws RemoteException {
		meuUsuario.criaPost(mensagem);
	}
	
	public String formataLinhaDoTempo() throws RemoteException {
		StringBuilder strBuilder = new StringBuilder();
		if (meuUsuario != null) {
			Set<Postagem> posts = meuUsuario.geraLinhaDoTempo(LocalDateTime.MIN, LocalDateTime.MAX);
			for (Postagem post : posts) {
				strBuilder.append(post + System.lineSeparator());
			}
		}
		return strBuilder.toString();
	}
	
	public void perfilUser(String NomeUser) throws RemoteException{
		Usuario usuario = servidor.getUsuarios().get(NomeUser);
		List<Postagem> list = usuario.getPosts();
		Collections.reverse(list);
		for (Postagem post : list) {
			System.out.println(post);
		}
	}
	
	public void enviarDM(String NomeUser, String mensagem) throws RemoteException {
		Usuario usuario = servidor.getUsuarios().get(NomeUser);
		Usuario autor = servidor.getUsuarios().get(meuUsuario.getNome());
		usuario.criaDirect(meuUsuario.getNome(), mensagem, "Received");
		autor.criaDirect(NomeUser, mensagem, "Sended");
	}
	
	public void verDM(String nome) throws RemoteException {
		Usuario autor = servidor.getUsuarios().get(meuUsuario.getNome());
		List<Direct> list = autor.getDirects();
		if (nome == null) {
			for (Direct direct : list) {
				System.out.println(direct);
			}
		}
		else {
			for (Direct direct : list) {
				if (direct.getNomeUsuario().equals(nome)) {
					System.out.println(direct);
				} 
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			String host = args.length < 1 ? "localhost" : args[0];
			
			ClienteTuiter cliente = new ClienteTuiter(host);
			String nomeUsuario = "";
			
			Scanner input = new Scanner(System.in);
			String line = "";
			System.out.print(COMANDOS);
			System.out.print(INIT_CHAR);
			while (!(line = input.nextLine().trim().toLowerCase())
					.equals("exit")) {
				
				StringTokenizer linha = new StringTokenizer(line, " ");
				if (linha.hasMoreTokens()) {
					String command = linha.nextToken();
					if (command.equals("/tl")) {
						System.out.println(cliente.formataLinhaDoTempo());
					} else if (command.equals("/login")) {
						Usuario usuario = cliente.login(linha.nextToken());
						nomeUsuario = usuario.getNome();
					} else if (command.equals("/post")) {
						String msg = linha.nextToken("");
						cliente.criaPostagem(msg);
					} else if (command.equals("/usuarios")) {
						System.out.println(cliente.listaUsuarios());
					} else if (command.equals("/seguir")) {
						String nomeOutroUsuario = linha.nextToken();
						cliente.segueUsuario(nomeOutroUsuario);
					} else if (command.equals("/seguindo")) {
						System.out.println(cliente.listaSeguidos());
					} else if (command.equals("/ajuda")) {
						System.out.println(COMANDOS);
					} else if (command.equals("/sair")) {
						System.exit(0);
					} else if (command.equals("/perfil")) {
						cliente.perfilUser(linha.nextToken());
					} else if (command.equals("/enviadm")) {
						System.out.println("Escreva sua mensagem");
						String mensagem = input.nextLine();
						cliente.enviarDM(linha.nextToken(), mensagem);
					} else if (command.equals("/verdm")) {
						// Se vocÍ n„o por nenhum nome de usu·rio, vai ser retornado todas as mensagens recebidas e enviadas.
						String nome = null;
						try {
							nome = linha.nextToken();
						} catch (Exception e) {}
						cliente.verDM(nome);
					} else {
						System.err.println("Comando desconhecido: " + command + "\n" + COMANDOS);
					}
				}
				System.out.print(nomeUsuario + INIT_CHAR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(1);
		}
	}
}
