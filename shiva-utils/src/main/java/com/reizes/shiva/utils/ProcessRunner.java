package com.reizes.shiva.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 외부 프로그램을 실행하는 클래스
 * 2009-10-21 stdin, stdout의 Writer, Reader 인터페이스를 얻는다.
 * 2010-05-24 stderr 추가
 * @author inho
 * @author reizes
 * @since 2009-07-03
 * @since 2010-05-24
 */
public class ProcessRunner {
	private static final String DEFAULT_ENCODING = "UTF-8";

	private final ProcessBuilder processBuilder;
	private final Map<String, String> environment;

	protected Logger log = Logger.getLogger(this.getClass());
	private Process process;

	/**
	 * 지정된 명령과 매개변수를 갖는 프로세스 빌더와 환경변수를 값을 초기화 한다.
	 * @param command 실행할 명령
	 * @param args 명령 실행에 필요한 매개변수
	 */
	public ProcessRunner(String command, String... args) {
		List<String> commands = new ArrayList<String>();
		commands.add(command);

		for (String arg : args) {
			commands.add(arg);
		}

		log.debug("process : " + commands);
		processBuilder = new ProcessBuilder(commands);
		environment = processBuilder.environment();
	}

	/**
	 * 지정된 명령과 매개변수를 갖는 프로세스 빌더와 환경변수를 값을 초기화 한다.
	 * @param command 실행할 명령
	 * @param args 명령 실행에 필요한 매개변수
	 */
	public ProcessRunner(String command) {

		log.debug("process : " + command);
		processBuilder = new ProcessBuilder(command);
		environment = processBuilder.environment();
	}

	/**
	 * 생성된 프로세스 빌더를 이용하여 프로세스를 실행한다.
	 * @throws IOException -
	 * @throws InterruptedException -
	 */
	public void start() throws IOException, InterruptedException {
		process = processBuilder.start();
	}

	/**
	 * 환경변수 값을 추가한다.
	 * @param key 환경변수 키
	 * @param value 환경변수 값
	 */
	public void insertEnvironment(String key, String value) {
		environment.put(key, value);
	}

	/**
	 * 환경변수 값을 제거한다.
	 * @param key 환경변수 키
	 */
	public void removeEnvironment(String key) {
		environment.remove(key);
	}

	/**
	 * @return 현재 프로세스의 작업 디렉토리
	 */
	public File getWorkDirectory() {
		return processBuilder.directory();
	}

	/**
	 * 현재 프로세스의 작업 디렉토리를 설정한다.
	 * @param directory 설정할 작업 디렉토리
	 */
	public void setWorkDirectory(File directory) {
		processBuilder.directory(directory);
	}

	/**
	 * 현재 프로세스에 값을 입력한다.
	 * @param input 입력할 스트링
	 * @param encoding 입력할 스트링의 인코딩
	 * @throws IOException -
	 */
	public void writeInput(String input, String encoding) throws IOException {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		BufferedWriter stdin = getStdInBufferedWriter(encoding);
		stdin.write(input);
		stdin.close();
	}

	/**
	 * 프로세스의 stdin BufferedWriter를 얻는다.
	 * @param encoding - 사용할 인코딩
	 * @return - Writer
	 * @throws UnsupportedEncodingException -
	 */
	public BufferedWriter getStdInBufferedWriter(String encoding) throws UnsupportedEncodingException {
		return new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), encoding == null ? DEFAULT_ENCODING
			: encoding));
	}

	/**
	 * 프로세스의 stdin BufferedWriter를 얻는다.
	 * @return - Writer
	 * @throws UnsupportedEncodingException -
	 */
	public BufferedWriter getStdInBufferedWriter() throws UnsupportedEncodingException {
		return getStdInBufferedWriter(null);
	}

	/**
	 * 현재 프로세스에 값을 입력한다. (디폴트 인코딩 사용 : UTF-8)
	 * @param input 입력할 스트링
	 * @throws IOException -
	 */
	public void writeInput(String input) throws IOException {
		writeInput(input, null);
	}

	/**
	 * 현재 프로세스의 출력 값을 가져온다.
	 * @param encoding 출력 값의 인코딩
	 * @return 현재 프로세스의 출력 값
	 * @throws IOException -
	 */
	public String readOutput(String encoding) throws IOException {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		StringBuilder string = new StringBuilder();
		String line = "";
		BufferedReader stdout = getStdOutBufferedReader(encoding);

		while ((line = stdout.readLine()) != null) {
			string.append(line).append("\n");
		}

		stdout.close();

		String result = string.toString();
		//return result.substring(0, result.lastIndexOf("\n"));
		return result;
	}

	/**
	 * 현재 프로세스의 stderr 값을 가져온다.
	 * @param encoding 출력 값의 인코딩
	 * @return 현재 프로세스의 출력 값
	 * @throws IOException -
	 */
	public String readError(String encoding) throws IOException {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		StringBuilder string = new StringBuilder();
		String line = "";
		BufferedReader stdout = getStdErrBufferedReader(encoding);

		while ((line = stdout.readLine()) != null) {
			string.append(line).append("\n");
		}

		stdout.close();

		String result = string.toString();
		//return result.substring(0, result.lastIndexOf("\n"));
		return result;
	}

	public String readError() throws IOException {
		return readError(null);
	}

	/**
	 * 프로세스의 stdout BufferedReader를 얻는다.
	 * @param encoding - stdout 인코딩
	 * @return - BufferedReader
	 * @throws UnsupportedEncodingException -
	 */
	public BufferedReader getStdOutBufferedReader(String encoding) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(process.getInputStream(), encoding == null ? DEFAULT_ENCODING
			: encoding));
	}

	/**
	 * 프로세스의 stdout BufferedReader를 얻는다.
	 * @return - BufferedReader
	 * @throws UnsupportedEncodingException -
	 */
	public BufferedReader getStdOutBufferedReader() throws UnsupportedEncodingException {
		return getStdOutBufferedReader(null);
	}

	/**
	 * 프로세스의 stderr BufferedReader를 얻는다.
	 * @param encoding - stdout 인코딩
	 * @return - BufferedReader
	 * @throws UnsupportedEncodingException -
	 */
	public BufferedReader getStdErrBufferedReader(String encoding) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(process.getErrorStream(), encoding == null ? DEFAULT_ENCODING
			: encoding));
	}

	public BufferedReader getStdErrBufferedReader() throws UnsupportedEncodingException {
		return getStdErrBufferedReader(null);
	}

	/**
	 * 현재 프로세스의 출력 값을 가져온다. (디폴트 인코딩 사용 : UTF-8)
	 * @return 현재 프로세스의 출력 값
	 * @throws IOException -
	 */
	public String readOutput() throws IOException {
		return readOutput(null);
	}

	/**
	 * 현재 프로세스를 종료시킨다.
	 */
	public void destroy() {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		process.destroy();
		process = null;
	}

	/**
	 * 현재 프로세스의 종료 값을 리턴한다.
	 * @return 현재 프로세스의 종료 값
	 */
	public int exitValue() {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		return process.exitValue();
	}

	/**
	 * 현재 프로세스가 종료될 때까지 대기한다.
	 * @return 현재 프로세스의 종료 값
	 * @throws InterruptedException -
	 */
	public int waitFor() throws InterruptedException {
		if (process == null) {
			throw new IllegalStateException("ProcessRunner.start() is not called.");
		}

		return process.waitFor();
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
}
