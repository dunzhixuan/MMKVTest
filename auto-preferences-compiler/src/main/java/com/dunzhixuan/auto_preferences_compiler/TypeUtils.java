package com.dunzhixuan.auto_preferences_compiler;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.dunzhixuan.auto_preferences_compiler.Consts.PARCELABLE;


/**
 * Utils for type exchange
 */
public class TypeUtils {

  private Types types;
  private Elements elements;
  private TypeMirror parcelableType;

  public TypeUtils(Types types, Elements elements) {
    this.types = types;
    this.elements = elements;

    parcelableType = this.elements.getTypeElement(PARCELABLE).asType();
  }

  /**
   * Diagnostics out the true java type
   *
   * @param element Raw type
   * @return Type class of java
   */
  public int typeExchange(Element element) {
    TypeMirror typeMirror = element.asType();

    // Primitive
    if (typeMirror.getKind().isPrimitive()) {
      return element.asType().getKind().ordinal();
    }

    switch (typeMirror.toString()) {
      case Consts.BYTE:
        return TypeKind.BYTE.ordinal();
      case Consts.SHORT:
        return TypeKind.SHORT.ordinal();
      case Consts.INTEGER:
        return TypeKind.INT.ordinal();
      case Consts.LONG:
        return TypeKind.LONG.ordinal();
      case Consts.FLOAT:
        return TypeKind.FLOAT.ordinal();
      case Consts.DOUBEL:
        return TypeKind.DOUBLE.ordinal();
      case Consts.BOOLEAN:
        return TypeKind.BOOLEAN.ordinal();
      case Consts.STRING:
        return TypeKind.STRING.ordinal();
      default: // Other side, maybe the PARCELABLE or OBJECT.
        if (types.isSubtype(typeMirror, parcelableType)) { // PARCELABLE
          return TypeKind.PARCELABLE.ordinal();
        } else if (element.asType().toString().equals("java.util.Set<java.lang.String>")) {//SETSTRING
          return TypeKind.SETSTRING.ordinal();
        } else { // For others
          return TypeKind.OBJECT.ordinal();
        }
    }
  }
}
