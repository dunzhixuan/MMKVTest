package com.dunzhixuan.auto_preferences_compiler;

/**
 * Kind of field type.
 */
public enum TypeKind {
  // Base type
  BOOLEAN,
  BYTE,
  SHORT,
  INT,
  LONG,
  CHAR,
  FLOAT,
  DOUBLE,

  // Other type
  STRING,
  PARCELABLE,
  SETSTRING,
  OBJECT;
}
