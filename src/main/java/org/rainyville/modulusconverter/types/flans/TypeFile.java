package org.rainyville.modulusconverter.types.flans;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeFile {
    public EnumType type;
    public String name, contentPack;
    private final ArrayList<String> lines;
    public static HashMap<EnumType, ArrayList<TypeFile>> files;
    private int readerPosition = 0;
    private int hash = 0x12345678;

    static {
        files = new HashMap<>();
        for (EnumType type : EnumType.values()) {
            files.put(type, new ArrayList<>());
        }

    }

    public TypeFile(String contentPack, EnumType t, String s) {
        this(contentPack, t, s, true);
    }

    public TypeFile(String contentPack, EnumType t, String s, boolean addToTypeFileList) {
        type = t;
        name = s;
        this.contentPack = contentPack;
        lines = new ArrayList<>();
        if (addToTypeFileList)
            files.get(type).add(this);
    }

    public void parseLine(String line) {
        lines.add(line);
        hash ^= line.hashCode();
    }

    public String readLine() {
        if (readerPosition == lines.size())
            return null;
        return lines.get(readerPosition++);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
