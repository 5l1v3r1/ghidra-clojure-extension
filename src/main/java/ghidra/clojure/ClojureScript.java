package ghidra.clojure;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import ghidra.app.script.GhidraScript;
import ghidra.app.services.ConsoleService;
import ghidra.framework.plugintool.PluginTool;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ClojureScript extends GhidraScript {

	static class ghidraHelper extends GhidraScript {
		@Override
		protected void run() throws Exception {

		}
	}

	@Override
	protected void run() throws Exception {
		PrintWriter out = getStdOut();
		PrintWriter err = getStdErr();
		
		//PrintStream outStream = new PrintStream(new ByteArrayOutputStream());
		PrintStream outStream = new PrintStream(new WriterOutputStream(out, StandardCharsets.UTF_8), true);	
		PrintStream errStream = new PrintStream(new WriterOutputStream(err, StandardCharsets.UTF_8), true);
		
		PrintStream stdout = System.out;
		PrintStream stderr = System.err;
		
		System.setOut(outStream);
		System.setErr(errStream);

		IFn eval = Clojure.var("clojure.core", "eval");
		eval.invoke(Clojure.read("(defn set-var [name content] (intern *ns* (symbol name) content))"));
		IFn setvar = Clojure.var("clojure.core", "set-var");

		// load ghidra API to clojure
		ClojureScript.ghidraHelper ghidraHelperInstance = new ClojureScript.ghidraHelper();
		setvar.invoke("currentAddress", currentAddress);
		setvar.invoke("currentHighlight", currentHighlight);
		setvar.invoke("currentLocation", currentLocation);
		setvar.invoke("currentSelection", currentSelection);
		setvar.invoke("currentProgram", currentProgram);
		setvar.invoke("state", state);
		setvar.invoke("monitor", monitor);	
		setvar.invoke("ghidra", ghidraHelperInstance);

		IFn loadfile = Clojure.var("clojure.core", "load-file");
		loadfile.invoke(this.sourceFile.getFile(false).getCanonicalPath());
		
		outStream.write('\n');
		errStream.write('\n');
		
		outStream.flush();
		errStream.flush();
		
		System.setOut(stdout);
		System.setErr(stderr);
	}
	
	private PrintWriter getStdOut() {
		PluginTool tool = state.getTool();
		if (tool != null) {
			ConsoleService console = tool.getService(ConsoleService.class);
			if (console != null) {
				return console.getStdOut();
			}
		}
		return new PrintWriter(System.out, true);
	}

	private PrintWriter getStdErr() {
		PluginTool tool = state.getTool();
		if (tool != null) {
			ConsoleService console = tool.getService(ConsoleService.class);
			if (console != null) {
				return console.getStdErr();
			}
		}
		return new PrintWriter(System.err, true);
	}
	
}
