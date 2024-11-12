package trt;

import javax.tools.JavaCompiler.CompilationTask;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class JavaParser {

	private static void compile() {

		CompilationUnit compilationUnit = new CompilationUnit();
		ClassOrInterfaceDeclaration myClass = compilationUnit.addClass("MyClass").setPublic(true);
		myClass.addField(int.class, "A_CONSTANT", Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC,
				Modifier.Keyword.FINAL);
		myClass.addField(String.class, "name", Modifier.Keyword.PRIVATE);
		String code = myClass.toString();
		System.out.println(code);

	}

	public static void main(String[] args) {
		System.out.println("HELLO");
		compile();
	}

}
