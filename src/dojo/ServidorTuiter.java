package dojo;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServidorTuiter implements Servidor {

	public static String NOME_SERVIDOR = "ServidorTuiter";
	
	private Map<String, Usuario> usuarios;
	
	public ServidorTuiter() {
		this.usuarios = new HashMap<String, Usuario>();
	}
	
	@Override
	public Map<String, Usuario> getUsuarios() {
		return this.usuarios;
	}

	@Override
	public Usuario conecta(String nomeUsuario) throws RemoteException {
		Usuario refRemotaUsuario = usuarios.get(nomeUsuario);
		if (refRemotaUsuario == null) {
			Usuario objUsuario = new UsuarioImpl(nomeUsuario);
			refRemotaUsuario = (Usuario) UnicastRemoteObject .exportObject(objUsuario, 0);
			usuarios.put(nomeUsuario, refRemotaUsuario);
		}
		return refRemotaUsuario;
	}
	
	public static void main(String[] args) {
		try {
			String host = "127.0.0.1";
			LocateRegistry.createRegistry(1099);
			ServidorTuiter objServidor = new ServidorTuiter();
			Servidor refRemotaServidor = (Servidor) UnicastRemoteObject.exportObject(objServidor, 0);
			Registry registry = LocateRegistry.getRegistry(host);
			registry.rebind(NOME_SERVIDOR, refRemotaServidor);
			System.err.println("Servidor esta rodando em " + InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
