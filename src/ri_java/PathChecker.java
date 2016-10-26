package ri_java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author james
 */
public class PathChecker {

    private final String path;
    private final String owner;
    private final String group;
    private final String digitalpermission;
    private boolean flag = false;

    public PathChecker(String input) {
     
     String[] result = input.split("\\s");
     path=result[0];
     owner=result[1];
     group=result[2];
     digitalpermission=result[3];
     if (result.length>=5){
         flag=Boolean.parseBoolean(result[4]);
     }

    }

    public void checkpath() throws IOException {
        Path path_p = Paths.get(path);
        if (flag == false) {
            if (!Files.exists(path_p)) {
                System.out.println("Path does not exist");
                System.exit(1);
            } else {
                checkowner();
                checkgroup();
                checkpermissions();
            }

        }
        if (flag == true) {

            setpath();
            setowner();
            setgroup();
            setpermissions();

        }

    }

    public void checkowner() throws IOException {
        Path path_p = Paths.get(path);
        PosixFileAttributes attr = Files.readAttributes(path_p, PosixFileAttributes.class
        );
        if (!owner.equals(String.valueOf(attr.owner()))) {
            System.out.println("Owner does not match. Current object owner: " + attr.owner());
            System.exit(1);
        }

    }

    public void checkgroup() throws IOException {
        Path path_p = Paths.get(path);
        PosixFileAttributes attr = Files.readAttributes(path_p, PosixFileAttributes.class);
        if (!group.equals(String.valueOf(attr.group()))) {
            System.out.println("Group does not match. Current object group: " + attr.group());
            System.exit(1);
        }
    }

    public void checkpermissions() throws IOException {
        StringBuilder unixperm = new StringBuilder();
        for (char c : digitalpermission.toCharArray()) {
            Integer temp = Character.getNumericValue(c);
            unixperm.append((temp & 4) == 0 ? '-' : 'r');
            unixperm.append((temp & 2) == 0 ? '-' : 'w');
            unixperm.append((temp & 1) == 0 ? '-' : 'x');
        }
        Path path_original = Paths.get(path);
        Set<PosixFilePermission> set = Files.getPosixFilePermissions(path_original);  //Tomasz - niech rzuci swiatlo co sie dzieje tutaj bo na czuja jest zrobione
        String set1 = PosixFilePermissions.toString(set);
        if (!set1.equals(String.valueOf(unixperm))) {
            System.out.println("Permissions does not match. Current object permission: " + set1);
            System.exit(1);
        }

    }

    public void setpath() {
        try {
        Path path_p =Paths.get(path);
        Files.createDirectories(path_p);
        } catch (Exception e) {
            System.out.println("Directory can't be created");
        }

    }

    private void setowner() throws IOException {
        Path path_p = Paths.get(path);
        UserPrincipal owner_temp = path_p.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(owner);
        try {
            Files.setOwner(path_p, owner_temp);
        } catch (Exception e) {
            System.out.println("Owner can't be changed to :" + owner);
            System.exit(1);
        }
    }

    private void setgroup() throws IOException {
        Path path_p = Paths.get(path);
        GroupPrincipal group_temp = path_p.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByGroupName(group);
        try {
            Files.getFileAttributeView(path_p, PosixFileAttributeView.class).setGroup(group_temp);
        } catch (Exception e) {
            System.out.println("Group can't be changed to :" + group);
            System.exit(1);
        }
    }

    public void setpermissions() throws IOException {
        Path path_p = Paths.get(path);
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        StringBuilder unixperm = new StringBuilder();
        for (char c : digitalpermission.toCharArray()) {
            Integer temp = Character.getNumericValue(c);
            unixperm.append((temp & 4) == 0 ? '-' : 'r');
            unixperm.append((temp & 2) == 0 ? '-' : 'w');
            unixperm.append((temp & 1) == 0 ? '-' : 'x');
        }
        Set<PosixFilePermission> posixpermission = PosixFilePermissions.fromString(unixperm.toString());
        try{
        Files.setPosixFilePermissions(path_p, posixpermission);
        }catch(Exception e){
            System.out.println("Permissions can't be changed to :" + posixpermission);
            System.exit(1);
        }
    }

    public String getpath() {
        return path;
    }

    public String getowner() {
        return owner;
    }

    public String getgroup() {
        return group;
    }

    public String getpermissions() {
        return digitalpermission;
    }

    public boolean getflag() {
        return flag;
    }

}
