package zimmerzuteilung.imports;

import zimmerzuteilung.objects.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ImportFileBak {
    private static HashMap<String, Integer> config = new HashMap<>();
    private static ArrayList<File> files = new ArrayList<>();
    private static HashMap<String, Building> buildings = new HashMap<>();
    // for blacklist: see ignoreHeading
    private static String[] blacklist = new String[] { "zimmer", "nummer", "kapazität", "geschlecht", "belegung" };

    public static void importBuildings(String pathToFolder) {
        try {
            File folder = new File(pathToFolder);
            System.out.println("Im Ordner gefundene Dateien: " +
                    folder.getAbsolutePath());
            File[] fileList = folder.listFiles();
            if (fileList.length == 0) {
                throw new FileNotFoundException();
            }
            for (File file : fileList) {
                if (file.getName().contains("config")) {
                    System.out.println("Config-Datei gefunden: "
                            + file.getAbsolutePath());
                    ImportFileBak.readConfig(file, ":");
                } else {
                    ImportFileBak.files.add(file);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Es wurde keine Datei in dem Ordner "
                    + pathToFolder + "gefunden.");
            e.printStackTrace();
        }
    }

    public static void readConfig(File file, String delimiter) {
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line != null && line.length() != 0 && line.charAt(0) != '#') {
                    String[] split = line.split(delimiter);
                    config.put(split[1].strip(), Integer.valueOf(split[0].strip()));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Die Datei "
                    + file.getAbsolutePath()
                    + " konnte nicht gefunden werden.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Room> importRooms(String delimiter, boolean ignoreHeading) {
        ArrayList<Room> rooms = new ArrayList<>();
        for (File file : ImportFileBak.files) {
            try {
                System.out.println("Datei einlesen: " + file.getAbsolutePath());
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String line = reader.nextLine().strip();
                    boolean firstLine = true;
                    if (line == null || line.length() == 0) {
                        continue;
                    }
                    if (ignoreHeading && firstLine) {
                        for (String word : ImportFileBak.blacklist) {
                            if (line.toLowerCase().contains(word)) {
                                // ignore this line
                                continue;
                            }
                        }
                    }
                    firstLine = false;
                    
                    String[] split = line.split(delimiter);

                    String buildingName = split[config.get("Internat") - 1];
                    String roomNumber = split[config.get("Raumbezeichnung") - 1];
                    String roomCapacity = split[config.get("Kapazität") - 1];

                    GENDER roomgender;
                    if (split[config.get("Geschlecht") - 1].toLowerCase().equals("m")) {
                        roomgender = GENDER.m;
                    } else if (split[config.get("Geschlecht") - 1].toLowerCase().equals("w")) {
                        roomgender = GENDER.f;
                    } else {
                        roomgender = GENDER.d;
                    }

                    // find or create building
                    Building building;
                    if (ImportFileBak.buildings.containsKey(buildingName)) {
                        building = ImportFileBak.buildings.get(buildingName);
                    } else {
                        building = new Building(buildingName);
                        ImportFileBak.buildings.put(buildingName, building);
                    }

                    // create and add room
                    if (file.getName().contains("Klausur")) {
                        int a = 34;
                    }
                    Room room = new Room(roomNumber, Integer.valueOf(roomCapacity), roomgender);
                    building.addRoom(room);
                    rooms.add(room);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Die Datei " + file.getAbsolutePath()
                        + " konnte nicht gefunden werden.");
                e.printStackTrace();
            }
        }
        return rooms;
    }
}
