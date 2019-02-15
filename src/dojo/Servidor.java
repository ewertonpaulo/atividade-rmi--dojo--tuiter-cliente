package dojo;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Map;

public interface Servidor extends Remote {

	public Usuario conecta(String nomeUsuario) throws RemoteException;
	
	public Map<String, Usuario> getUsuarios() throws RemoteException;
	
}
