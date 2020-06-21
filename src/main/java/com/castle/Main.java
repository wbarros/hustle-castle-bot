package com.castle;

import com.castle.config.HustleCastleBotConfig;
import com.castle.play.Arena;
import com.castle.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.castle.config.HustleCastleBotConfig.loadConfig;

// comando para instalar lib jnativehook mvn deploy:deploy-file -Durl=file://C:\jni\jnativehook-local\ -Dfile=./jnativehook-2.1.0.jar -DgroupId=org -DartifactId=jnativehook -Dpackaging=jar -Dversion=1.1.4

// https://github.com/bytedeco/javacpp-presets OPEN CV como instalar

public class Main extends JPanel{
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	JTextPane textPane = new JTextPane();
	public static boolean continuar = true;
	private final String BOTAO_PARTICIPAR_ARENA_FRUTAS = "arena/botao_arena_participar_comida.png";
	private final String BOTAO_PARTICIPAR_ARENA_BILHETE = "arena/botao_arena_participar_bilhete.png";
	private final String BOTAO_PARTICIPAR_ARENA_CONFIRMAR = "arena/botao_arena_confirmar_participacao.png";
	private final String BOTAO_ARENA_CANCELAR = "arena/botao_arena_cancelar.png";
	private final String BOTAO_ARENA_SEM_COMIDA = "arena/botao_arena_sem_comida.png";
	private boolean teste = false;

	public static void main(String[] args) throws InterruptedException {

		if(args.length == 0) {
			logger.error("Executar como \"java -jar arquivo.jar arquivo.yml\"");
			Thread.sleep(5000);
			System.exit(0);
		}

		try {
			loadConfig(args[0]);
			SwingUtilities.invokeLater(() -> createAndShowGui());
		} catch (Exception e) {
			logger.error("Exception : ", e);
		}

	}

	public Main() {
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;

		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JButton button = new JButton("Iniciar");
		JButton button2 = new JButton("Parar");
		JPanel btnPanel = new JPanel(new GridBagLayout());
		btnPanel.add(button, gbc);
		btnPanel.add(button2, gbc);

		JScrollPane areaScrollPane = new JScrollPane(textPane);
		textPane.setEditable(false);
		areaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(300, 290));
		btnPanel.add(areaScrollPane, null);
		redirectSystemStreams();

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				start();
			}
		});

		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Main.continuar = false;
				System.out.println("Parei.");
				textPane.setText("");
			}
		});

		setLayout(new BorderLayout());
		add(btnPanel, BorderLayout.PAGE_END);
	}

	private void start(){
		SwingWorker worker = new SwingWorker() {
			@Override
			protected Void doInBackground() throws Exception {
				HustleCastleBotConfig config = HustleCastleBotConfig.getInstance();
				System.out.println("Poder atual: "+ config.getMyPower());
				System.out.println("Estrategia escolhida: "+ config.getSeletorEstrategia());
				System.out.println("x: "+ config.getPowerCut().get("x") +
						" y: "+ config.getPowerCut().get("y") +
						" width: "+ config.getPowerCut().get("width")
				+ " height: "+ config.getPowerCut().get("height"));
				Main.continuar = true;
				if(!teste) {
					while (Main.continuar) {
						boolean aptoParaBatalha = false;
						try {
							if (Utils.encontraImagemParaClicar(BOTAO_PARTICIPAR_ARENA_FRUTAS)) {
								System.out.println("Participar clicado.");
								Thread.sleep(2000);
								if (!Utils.encontraImagemParaClicar(BOTAO_ARENA_SEM_COMIDA)) {
									aptoParaBatalha = true;
								} else {
									System.out.println("Sem comida.");
								}
							} else if(Utils.encontraImagemParaClicar(BOTAO_PARTICIPAR_ARENA_BILHETE)) {
								System.out.println("Participar bilhete clicado.");
								Thread.sleep(2000);
								if (!Utils.encontraImagemParaClicar(BOTAO_ARENA_SEM_COMIDA)) {
									aptoParaBatalha = true;
								} else {
									System.out.println("Sem bilhete.");
								}
							}
							if (aptoParaBatalha) {
								Thread.sleep(4000);
								if (Utils.encontraImagemParaClicar(BOTAO_PARTICIPAR_ARENA_CONFIRMAR))
									System.out.println("Confirmar clicado.");
								Thread.sleep(3000);
								Utils.esperarEventoAcontecerCliqueNaoOcioso(BOTAO_ARENA_CANCELAR, "Esperando arena comercar...");
								Arena.getInstance().iniciarArena();
							}
							Thread.sleep(8000);
							textPane.setText("");
							Utils.sairDoModoOcioso();
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
					}
				}else {
					Testes.teste1();
				}
				return null;
			}
		};

		worker.execute();
	}

	private static void createAndShowGui() {
		JFrame frame = new JFrame("Bot Hustle Castle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Main());
		frame.pack();
		frame.setSize(346,400);
		frame.setLocation(1020,0);
		frame.setVisible(true);
	}

	private void updateTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Document doc = textPane.getDocument();
				try {
					doc.insertString(doc.getLength(), text, null);
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
				textPane.setCaretPosition(doc.getLength() - 1);
			}
		});
	}

	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(final int b) throws IOException {
				updateTextPane(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextPane(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

}



