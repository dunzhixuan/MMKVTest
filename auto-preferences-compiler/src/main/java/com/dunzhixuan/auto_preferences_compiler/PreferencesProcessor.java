package com.dunzhixuan.auto_preferences_compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import me.zeyuan.lib.autopreferences.annotations.Preferences;

@AutoService(Processor.class)
public class PreferencesProcessor extends AbstractProcessor {
  private Messager messager;
  private Elements elementUtils;
  private Filer filer;
  private static final String FILE_NAME = "FILE_NAME";
  private static final String CLASS_NAME_SUFFIX = "Preferences";
  private TypeUtils typeUtils;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnvironment) {
    super.init(processingEnvironment);
    messager = processingEnvironment.getMessager();
    elementUtils = processingEnvironment.getElementUtils();
    filer = processingEnvironment.getFiler();
    typeUtils = new TypeUtils(processingEnvironment.getTypeUtils(), elementUtils);
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportAnnotations = new LinkedHashSet<>();
    supportAnnotations.add(Preferences.class.getCanonicalName());
    return supportAnnotations;
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Preferences.class)) {
      if (element.getKind() != ElementKind.INTERFACE) {
        messager.printMessage(
            Diagnostic.Kind.ERROR, "@Preferences can only be applied attach to interface", element);
      }

      JavaFile javaFile = createJavaFile(element);
      try {
        javaFile.writeTo(filer);
      } catch (IOException e) {
        messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
      }
    }
    return false;
  }

  private JavaFile createJavaFile(Element element) {
    String className = element.getSimpleName().toString();

    TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className + CLASS_NAME_SUFFIX);
    typeSpecBuilder.addModifiers(Modifier.PUBLIC);
    typeSpecBuilder.addField(createField_FILENAME(element));
    typeSpecBuilder.addField(createField_MMKV(element));
    typeSpecBuilder.addMethod(buildSetFileNameMethod());
    typeSpecBuilder.addMethod(buildGetFileNameMethod());
    typeSpecBuilder.addMethod(buildGetPreferencesMethod());
    typeSpecBuilder.addMethods(getOptionsMethods(element));

    JavaFile.Builder javaFileBuiler =
        JavaFile.builder(getPackageName(element), typeSpecBuilder.build());
    return javaFileBuiler.build();
  }

  private FieldSpec createField_FILENAME(Element element) {
    String fileName = getFiledName(element);
    FieldSpec.Builder fieldSpecBuilder =
        FieldSpec.builder(String.class, FILE_NAME)
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .initializer("$S", fileName);
    return fieldSpecBuilder.build();
  }

  private FieldSpec createField_MMKV(Element element) {
    ClassName className = ClassName.get("com.tencent.mmkv", "MMKV");

    CodeBlock codeBlock = CodeBlock.builder().add("MMKV.defaultMMKV()", className).build();
    FieldSpec.Builder fieldSpecBuilder =
        FieldSpec.builder(className, "mmkv")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .initializer(codeBlock);
    return fieldSpecBuilder.build();
  }

  private MethodSpec buildSetFileNameMethod() {
    return MethodSpec.methodBuilder("setPreferencesFileName")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(TypeName.VOID)
        .addParameter(String.class, "name")
        .addStatement(FILE_NAME + " = name")
        .addStatement("mmkv = MMKV.mmkvWithID(name)")
        .build();
  }

  private MethodSpec buildGetFileNameMethod() {
    return MethodSpec.methodBuilder("getPreferencesFileName")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addStatement("return " + FILE_NAME)
        .returns(String.class)
        .build();
  }

  private MethodSpec buildGetPreferencesMethod() {
    ClassName className = ClassName.get("android.content", "SharedPreferences");
    ClassName parameterClassName = ClassName.get("android.content", "Context");
    return MethodSpec.methodBuilder("getPreferences")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(parameterClassName, "context")
        .returns(className)
        .addStatement("return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)")
        .build();
  }

  private ArrayList<MethodSpec> getOptionsMethods(Element element) {
    ArrayList<MethodSpec> methodSpecs = new ArrayList<>();

    for (Element field : element.getEnclosedElements()) {
      MethodSpec getterMethodSpec = createGetterMethod(field);
      methodSpecs.add(getterMethodSpec);
      MethodSpec setterMethodSpec = createSetterMethod(field);
      methodSpecs.add(setterMethodSpec);
      MethodSpec removeMethodSpec = createRemoveMethod(field);
      methodSpecs.add(removeMethodSpec);
    }

    return methodSpecs;
  }

  private MethodSpec createGetterMethod(Element element) {
    TypeMirror type = element.asType();
    ClassName parameterClassName = ClassName.get("android.content", "Context");
    String typeAction = getTypeAction(element);
    String keyName = getNameOfPreferences(element);
    Object defaultName = getDefaultValue(element);

    return MethodSpec.methodBuilder(getterNameFormat(element.getSimpleName().toString()))
        .addJavadoc(
            (elementUtils.getDocComment(element) == null ? "" : elementUtils.getDocComment(element))
                + "\n")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(parameterClassName, "context")
        .returns(ClassName.get(type))
        .addStatement("return mmkv.decode$L($S,$L)", typeAction, keyName, defaultName)
        .build();
  }

  private MethodSpec createSetterMethod(Element element) {
    ClassName parameterClassName = ClassName.get("android.content", "Context");
    TypeMirror typeMirror = element.asType();
    String keyName = getNameOfPreferences(element);

    return MethodSpec.methodBuilder(setterNameFormat(element.getSimpleName().toString()))
        .addJavadoc(
            (elementUtils.getDocComment(element) == null ? "" : elementUtils.getDocComment(element))
                + "\n")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(parameterClassName, "context")
        .addParameter(ClassName.get(typeMirror), "value")
        .addStatement("mmkv.encode($S, value)", keyName)
        .build();
  }

  private MethodSpec createRemoveMethod(Element element) {
    ClassName parameterClassName = ClassName.get("android.content", "Context");
    String keyName = getNameOfPreferences(element);

    return MethodSpec.methodBuilder(removeNameFormat(element.getSimpleName().toString()))
        .addJavadoc(
            (elementUtils.getDocComment(element) == null ? "" : elementUtils.getDocComment(element))
                + "\n")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(parameterClassName, "context")
        .addStatement("mmkv.remove($S)", keyName)
        .build();
  }

  private String getTypeAction(Element element) {
    int type = typeUtils.typeExchange(element);
    if (type == TypeKind.BOOLEAN.ordinal()) {
      return "Bool";
    } else if (type == TypeKind.INT.ordinal()) {
      return "Int";
    } else if (type == TypeKind.LONG.ordinal()) {
      return "Long";
    } else if (type == TypeKind.FLOAT.ordinal()) {
      return "Float";
    } else if (type == TypeKind.DOUBLE.ordinal()) {
      return "Double";
    } else if (type == TypeKind.STRING.ordinal()) {
      return "String";
    } else if (type == TypeKind.PARCELABLE.ordinal()) {
      return "Parcelable";
    } else {
      if (element.asType().toString().equals("java.util.Set<java.lang.String>")) {
        return "StringSet";
      }
      return "null";
    }
  }

  private String getNameOfPreferences(Element field) {
    Preferences preferences = field.getAnnotation(Preferences.class);
    if (preferences == null || preferences.value().isEmpty()) {
      return field.getSimpleName().toString();
    } else {
      return preferences.value();
    }
  }

  private Object getDefaultValue(Element field) {
    Object defValue = ((VariableElement) field).getConstantValue();
    if (defValue == null) {
      return "null";
    }
    if (defValue instanceof String) {
      String stringValue = (String) defValue;
      if (stringValue.isEmpty()) {
        return "\"\"";
      } else {
        return "\"" + defValue + "\"";
      }
    } else if (defValue instanceof Long) {
      return defValue + "L";
    } else if (defValue instanceof Float) {
      return defValue + "f";
    } else if (defValue instanceof Double) {
      return defValue + "d";
    }

    return defValue;
  }

  private String getFiledName(Element element) {
    Preferences preferences = element.getAnnotation(Preferences.class);
    if (preferences.value().isEmpty()) return element.getSimpleName().toString();
    else return preferences.value();
  }

  private String getPackageName(Element element) {
    return elementUtils.getPackageOf(element).getQualifiedName().toString();
  }

  private String setterNameFormat(String key) {
    String result = key;
    if (key.startsWith("is")) {
      result = key.replace("is", "");
    }
    result = capitalize(result);
    return "set" + result;
  }

  private String getterNameFormat(String name) {
    if (!name.startsWith("is")) {
      name = capitalize(name);
      return "get" + name;
    }
    return name;
  }

  private String removeNameFormat(String name) {
    name = capitalize(name);
    return "remove" + name;
  }

  private String capitalize(String text) {
    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }

}
