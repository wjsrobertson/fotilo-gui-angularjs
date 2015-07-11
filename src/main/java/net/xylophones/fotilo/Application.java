package net.xylophones.fotilo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ServerSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.misc.RequestProcessor;

public class Application {

	final int port = 8081;
	
	private final Log log = LogFactory.getLog(Application.class);
	
	private final int numConcurrentConnections = 1;
	
	private ExecutorService service = Executors.newFixedThreadPool(numConcurrentConnections);

	private volatile boolean running = true;
	
	private static ApplicationContext appCtx;

	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		appCtx = new ClassPathXmlApplicationContext("appCtx-fotilo-core.xml");
		
		Application application = (Application)  appCtx.getBean("application");
		application.start();
	}
	
	private void start() {
		log.debug("starting application");
		log.debug("limiting to " + numConcurrentConnections + " concurrent requests");
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
		} catch (IOException e) {
			log.error(e);
			return;
		}
		
		if (serverSocket == null) {
			log.error("Couldn't create socket");
			return;
		}
		
		while (running) {
			try {
				log.debug("waiting for connection");
				Socket socket = serverSocket.accept();
				processConnection(socket);
			} catch (IOException e) {
				log.error("Error dealing with comnnection", e);
				break;
			}
		}
		
		log.debug("exiting application");
	}
	
	/**
	 * 
	 * 
	 * @param socket
	 */
	private void processConnection(Socket socket) throws IOException {
		log.debug("processing connection");
		/*
		RequestProcessor requestProcessor = createRequestProcessor();
		requestProcessor.setDownstreamSocket(socket);
		Future<Boolean> task = service.submit( requestProcessor );
		*/
	}
	
	/**
	 * Will be over-ridden using spring method injection
	 * 
	 * @return
	 */
	public RequestProcessor createRequestProcessor() {
		return null;
	}
}
