package dojo;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Direct implements Comparable<Direct>, Serializable{
	
	private static final long serialVersionUID = 1L;
	private String nomeUsuario;
	private String mensagem;
	private String status;
	private LocalDateTime timestamp;

	public Direct(String nomeUsuario, String mensagem, String status) {
		this(nomeUsuario, mensagem, status, LocalDateTime.now());
	}
	
	public Direct(String nomeUsuario, String mensagem, String status, LocalDateTime timestamp) {
		this.nomeUsuario = nomeUsuario;
		this.mensagem = mensagem;
		this.status = status;
		this.timestamp = timestamp;
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	@Override
	public int compareTo(Direct direct) {
		return this.timestamp.compareTo(direct.timestamp);
	}
	
	@Override
	public String toString() {
		return "[" + timestamp + "] "+ "<" + nomeUsuario + "> " + mensagem + ": " + status;
	}

}
