package cudl;

import static cudl.InternalInterpreter.BLIND_TRANSFER_SUCCESSSS;
import static cudl.InternalInterpreter.CALLER_HUNGUP_DURING_TRANSFER;
import static cudl.InternalInterpreter.DESTINATION_BUSY;
import static cudl.InternalInterpreter.DESTINATION_HANGUP;
import static cudl.InternalInterpreter.DTMF;
import static cudl.InternalInterpreter.EVENT;
import static cudl.InternalInterpreter.MAX_TIME_DISCONNECT;
import static cudl.InternalInterpreter.NETWORK_BUSY;
import static cudl.InternalInterpreter.NOANSWER;
import static cudl.InternalInterpreter.START;
import static cudl.InternalInterpreter.TALK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cudl.utils.Utils;

public class Interpreter {
	private InternalInterpreter internalInterpreter;
	private InterpreterContext context;

	public Interpreter(String url) throws IOException, ParserConfigurationException, SAXException {
		context = new InterpreterContext(url);
		internalInterpreter = new InternalInterpreter(context);
	}

	public void start() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(START, null);
	}

	public void noInput() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(EVENT, "noinput");
	}

	public void noMatch() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(EVENT, "nomatch");
	}

	public void disconnect() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(EVENT, "connection.disconnect.hangup");
	}

	public void blindTransferSuccess() throws IOException, SAXException,
			ParserConfigurationException {
		internalInterpreter.interpret(BLIND_TRANSFER_SUCCESSSS, null);
	}

	public void noAnswer() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(NOANSWER, null);
	}

	public void callerHangupDuringTransfer() throws IOException, SAXException,
			ParserConfigurationException {
		internalInterpreter.interpret(CALLER_HUNGUP_DURING_TRANSFER, null);
	}

	public void networkBusy() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(NETWORK_BUSY, null);
	}

	public void destinationBusy() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(DESTINATION_BUSY, null);
	}

	public void transferTimeout() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(MAX_TIME_DISCONNECT, null);
	}

	public void talk(String sentence) throws UnsupportedEncodingException, IOException,
			SAXException, ParserConfigurationException {
		String utterance = "'" + URLEncoder.encode(sentence, "UTF-8").replaceAll("'", "") + "'";
		internalInterpreter.interpret(TALK, utterance);
	}

	public void submitDtmf(String dtmf) throws IOException, SAXException,
			ParserConfigurationException {
		String utterance = "'" + dtmf.replaceAll(" ", "") + "'";
		internalInterpreter.interpret(DTMF, utterance);
	}

	public List<String> getLogsWithLabel(String... label) {
		List<String> logs = new ArrayList<String>();
		for (Iterator<Log> iterator = context.getLogs().iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			for (int i = 0; i < label.length; i++) {
				if (log.label.equals(label[i])) {
					logs.add(log.value);
				}
			}
		}
		return logs;
	}

	public List<String> getLogs() {
		List<String> logs = new ArrayList<String>();
		for (Iterator<Log> iterator = context.getLogs().iterator(); iterator.hasNext();) {
			logs.add(((Log) iterator.next()).value);
		}

		return logs;
	}

	public boolean hungup() {
		return context.isHangup();
	}

	public List<Prompt> getPrompts() {
		return context.getPrompts();
	}

	public String getTranferDestination() {
		return context.getTransferDestination();
	}

	public String getActiveGrammar() {
		return Utils.getNodeAttributeValue(context.getGrammarActive().get(0), "src").trim();
	}

	public Properties getCurrentDialogProperties() {
		return internalInterpreter.getCurrentDialogProperties();
	}

	public void destinationHangup() throws IOException, SAXException, ParserConfigurationException {
		internalInterpreter.interpret(DESTINATION_HANGUP, null);
	}
}
