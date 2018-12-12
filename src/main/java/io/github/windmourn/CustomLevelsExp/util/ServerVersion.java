package io.github.windmourn.CustomLevelsExp.util;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerVersion {

    private static final ServerVersion instance;
    private static int bukkitVersion;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(packageName.substring(packageName.lastIndexOf('.') + 1));
        try {
            bukkitVersion = Integer.parseInt(m.replaceAll(""));
        } catch (Exception e) {
            bukkitVersion = 0;
        }
        String verStr = Bukkit.getVersion().split("MC: ")[1];
        verStr = verStr.substring(0, verStr.length() - 1);
        instance = new ServerVersion(verStr);
    }

    private final String version;
    private final Integer[] versionPart;
    private final long value;

    public ServerVersion(String version) {
        this.version = version;
        this.versionPart = parseVersionPart(this.version);
        long v = 0;
        for (int i = 0; i < this.versionPart.length; i++) {
            v += Math.pow(100, this.versionPart.length - i - 1) * this.versionPart[i];
        }
        this.value = v;
    }

    public static ServerVersion getServerVersion() {
        return instance;
    }

    private static Integer[] parseVersionPart(String verionString) {
        Integer[] versionPart;
        String[] sp = verionString.split("\\.");
        while (sp.length < 3) {
            verionString += ".0";
            sp = verionString.split("\\.");
        }
        versionPart = new Integer[sp.length];
        for (int i = 0; i < sp.length; i++) {
            try {
                versionPart[i] = Integer.parseInt(sp[i]);
            } catch (Exception e) {
                versionPart[i] = 0;
            }
        }
        return versionPart;
    }

    private static String concatArray(String concatSymbol, Object[] objs) {
        return concatArray(concatSymbol, 0, objs);
    }

    private static String concatArray(String concatSymbol, int startIndex, Object[] objs) {
        String string = "";
        for (int i = startIndex; i < objs.length; i++) {
            string += concatSymbol + objs[i].toString();
        }
        return string.isEmpty() ? string : string.substring(concatSymbol.length());
    }

    public long getValue() {
        return value;
    }

    public Integer[] getVersionPart() {
        return versionPart.clone();
    }

    public boolean isGreaterThanOrEqual(String ver) {
        ServerVersion version = new ServerVersion(ver);
        return this.value >= version.value;
    }

    public boolean isLessThanOrEqual(String ver) {
        ServerVersion version = new ServerVersion(ver);
        return this.value <= version.value;
    }

    public boolean isGreaterThan(String ver) {
        ServerVersion version = new ServerVersion(ver);
        return this.value > version.value;
    }

    public boolean isLessThan(String ver) {
        ServerVersion version = new ServerVersion(ver);
        return this.value < version.value;
    }

    @Override
    public boolean equals(Object obj) {
        ServerVersion version;
        if (obj instanceof ServerVersion) {
            version = (ServerVersion) obj;
        } else {
            version = new ServerVersion(obj.toString());
        }
        return this.value == version.value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return concatArray(".", versionPart);
    }

}
