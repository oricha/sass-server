package com.stack.sass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;

/**
 * 
 * @author kmuniz
 *
 */
public class SassCompiler {
	
	private static Logger LOG = LoggerFactory.getLogger(SassCompiler.class);
	
	private String includePaths;
	private io.bit3.jsass.OutputStyle outputStyle = io.bit3.jsass.OutputStyle.EXPANDED;
	private boolean generateSourceComments;
	private boolean generateSourceMap;
	private boolean omitSourceMappingURL;
	private boolean embedSourceMapInCSS;
	private boolean embedSourceContentsInSourceMap;
	private SassCompiler.InputSyntax inputSyntax;
	private int precision;
	private String includePath = null;
	private boolean omitSourceMapingURL = false;

	/**
	 * All paths passed to this method must be relative to the same directory.
	 */
	public Output compileFile(
			String inputPathAbsolute, //
			String outputPathRelativeToInput, //
			String sourceMapPathRelativeToInput //

	) throws CompilationException {

		URI inputFile = new File(inputPathAbsolute).toURI();
		URI outputFile = new File(outputPathRelativeToInput).toURI();

		Options opt = getConfiguredOptions(inputPathAbsolute, sourceMapPathRelativeToInput);

		io.bit3.jsass.Compiler c = new io.bit3.jsass.Compiler();

		return c.compileFile(inputFile, outputFile, opt);
	}

	private Options getConfiguredOptions(String inputPathAbsolute, String sourceMapPathRelativeToInput) {
		Options opt = new Options();

		if(includePaths != null) {
			for (String path : includePaths.split(File.pathSeparator)) {
				opt.getIncludePaths().add(new File(path));
			}
		}
		String allIncludePaths = new File(inputPathAbsolute).getParent();
		opt.getIncludePaths().add(new File(allIncludePaths));

		opt.setIsIndentedSyntaxSrc(inputSyntax == InputSyntax.sass);
		opt.setOutputStyle(outputStyle);

		opt.setSourceComments(generateSourceComments);
		opt.setPrecision(precision);


		if (generateSourceMap) {
			opt.setSourceMapFile(new File(sourceMapPathRelativeToInput).toURI());
			opt.setSourceMapContents(embedSourceContentsInSourceMap);
			opt.setSourceMapEmbed(embedSourceMapInCSS);
			opt.setOmitSourceMapUrl(omitSourceMappingURL);
		} else {
			opt.setSourceMapContents(false);
			opt.setSourceMapEmbed(false);
			opt.setOmitSourceMapUrl(true);
		}
		return opt;
	}
	
	public SassCompiler initCompiler() {
		SassCompiler compiler = new SassCompiler();
		compiler.setEmbedSourceMapInCSS(this.embedSourceMapInCSS);
		compiler.setEmbedSourceContentsInSourceMap(this.embedSourceContentsInSourceMap);
		compiler.setGenerateSourceComments(this.generateSourceComments);
		compiler.setGenerateSourceMap(this.generateSourceMap);
		compiler.setIncludePaths(this.includePath);
		compiler.setInputSyntax(this.inputSyntax);
		compiler.setOmitSourceMappingURL(this.omitSourceMapingURL );
		compiler.setOutputStyle(this.outputStyle);
		compiler.setPrecision(this.precision);
		return compiler;
	}
	
	public void validateConfig() {
		if (!generateSourceMap) {
			if (embedSourceMapInCSS) {
				LOG.warn("embedSourceMapInCSS=true is ignored. Cause: generateSourceMap=false");
			}
			if (embedSourceContentsInSourceMap) {
				LOG.warn("embedSourceContentsInSourceMap=true is ignored. Cause: generateSourceMap=false");
			}
		}
	}

	public void setEmbedSourceMapInCSS(final boolean embedSourceMapInCSS) {
		this.embedSourceMapInCSS = embedSourceMapInCSS;
	}
	
	public void writeContentToFile(Path outputFilePath, String content) throws IOException {
		File file = outputFilePath.toFile();
		file.getParentFile().mkdirs();
		file.createNewFile();
		OutputStreamWriter os = null;
		try {
			os = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			os.write(content);
			os.flush();
		} finally {
			if (os != null)
				os.close();
		}
		LOG.debug("Written to: " + file);
	}

	public void setEmbedSourceContentsInSourceMap(final boolean embedSourceContentsInSourceMap) {
		this.embedSourceContentsInSourceMap = embedSourceContentsInSourceMap;
	}

	public void setGenerateSourceComments(final boolean generateSourceComments) {
		this.generateSourceComments = generateSourceComments;
	}

	public void setGenerateSourceMap(final boolean generateSourceMap) {
		this.generateSourceMap = generateSourceMap;
	}

	public void setIncludePaths(final String includePaths) {
		this.includePaths = includePaths;
	}

	public void setInputSyntax(final InputSyntax inputSyntax) {
		this.inputSyntax = inputSyntax;
	}

	public void setOmitSourceMappingURL(final boolean omitSourceMappingURL) {
		this.omitSourceMappingURL = omitSourceMappingURL;
	}

	public void setOutputStyle(final io.bit3.jsass.OutputStyle outputStyle) {
		this.outputStyle = outputStyle;
	}

	public void setOutputStyle(final OutputStyle outputStyle) {
		this.outputStyle = io.bit3.jsass.OutputStyle.values()[outputStyle.ordinal()];
	}

	public static enum OutputStyle {
		nested, expanded, compact, compressed
	}

	public void setPrecision(final int precision) {
		this.precision = precision;
	}


	public static enum InputSyntax {
		sass, scss
	}
}
