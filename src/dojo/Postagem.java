package dojo;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Postagem implements Comparable<Postagem>, Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nomeUsuario;
	private String mensagem;
	private LocalDateTime timestamp;

	public Postagem(String nomeUsuario, String mensagem) {
		this(nomeUsuario, mensagem, LocalDateTime.now());
	}
	
	public Postagem(String nomeUsuario, String mensagem, LocalDateTime timestamp) {
		this.nomeUsuario = nomeUsuario;
		this.mensagem = mensagem;
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
	public int compareTo(Postagem post) {
		return this.timestamp.compareTo(post.timestamp);
	}
	
	@Override
	public String toString() {
		return "[" + timestamp + "] "+ "<" + nomeUsuario + ">" + mensagem;
	}
}
