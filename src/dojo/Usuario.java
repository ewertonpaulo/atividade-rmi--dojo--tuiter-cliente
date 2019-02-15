package dojo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface Usuario extends Remote {
	
	public String getNome() throws RemoteException;
	
	public List<Postagem> getPosts() throws RemoteException;
	
	public List<Direct> getDirects() throws RemoteException;
	
	public List<Usuario> getSeguidos() throws RemoteException;
	
	public Postagem criaPost(String mensagem) throws RemoteException;
	
	public List<Postagem> filtraPosts(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) throws RemoteException;
	
	public Set<Postagem> geraLinhaDoTempo(LocalDateTime inicio, LocalDateTime fim) throws RemoteException;

	public void seguirOutroUsuario(Usuario outroUsuario) throws RemoteException;
	
	public Direct criaDirect(String nomeUser, String mensagem, String status) throws RemoteException;

}
