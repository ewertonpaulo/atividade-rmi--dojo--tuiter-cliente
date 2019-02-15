package dojo;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioImplTest {
	
	private Usuario usuario1;
	private Usuario usuario2;

	@BeforeEach
	void setUp() throws Exception {
		this.usuario1 = new UsuarioImpl("usuario1");
		this.usuario2 = new UsuarioImpl("usuario2");
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testCriaPosts() {
		try {
			assertEquals(0, usuario1.getPosts().size());
			String msg1 = "mensagem 1";
			Postagem post1 = usuario1.criaPost(msg1);	
			assertEquals(msg1, post1.getMensagem());
			assertEquals(1, usuario1.getPosts().size());
			assertEquals(msg1, usuario1.getPosts().iterator().next().getMensagem());
			assertEquals(post1, usuario1.getPosts().iterator().next());
			
			String msg2 = "mensagem 2";
			Postagem post2 = usuario1.criaPost(msg2);
			assertEquals(2, usuario1.getPosts().size());
			assertTrue(post1.getTimestamp().isBefore(post2.getTimestamp()));
		} catch (RemoteException e) {
			fail("Esta excecao nao deveria ser lancada");
		}
	}

	@Test
	void testFiltraPosts() {
		try {
			LocalDateTime tempo1 = LocalDateTime.now();
			System.out.println("Tempo 1: " + tempo1);
			Thread.sleep(1000);
			LocalDateTime tempo2 = LocalDateTime.now();
			System.out.println("Tempo 2: " + tempo2);
			assertEquals(0, usuario1.filtraPosts(tempo1, tempo2).size());
			
			Thread.sleep(1000);
			Postagem post1 = usuario1.criaPost("mensagem 1");
			assertEquals(0, usuario1.filtraPosts(tempo1, tempo2).size());

			Thread.sleep(1000);
			LocalDateTime tempo3 = LocalDateTime.now();
			System.out.println("Tempo 3: " + tempo3);
			Postagem post2 = usuario1.criaPost("mensagem 2");

			LocalDateTime tempo4 = LocalDateTime.now();
			System.out.println("Tempo 4: " + tempo4);
			
			List<Postagem> filtrados1 = usuario1.filtraPosts(tempo2, tempo3);
			assertEquals(1, filtrados1.size());
			assertEquals(post1, filtrados1.iterator().next());
			
			List<Postagem> filtrados2 = usuario1.filtraPosts(tempo3, tempo4);
			assertEquals(1, filtrados2.size());
			assertEquals(post2, filtrados2.iterator().next());
			
			List<Postagem> filtrados3 = usuario1.filtraPosts(tempo1, tempo4);
			assertEquals(2, filtrados3.size());
			Iterator<Postagem> iteratorFiltrados3 = filtrados3.iterator();
			assertEquals(post1, iteratorFiltrados3.next());
			assertEquals(post2, iteratorFiltrados3.next());
		} catch (RemoteException | InterruptedException e) {
			fail("Esta excecao nao deveria ser lancada");
		}
	}

}
