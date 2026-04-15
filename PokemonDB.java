import java.util.*;
import java.io.*;

class Pokemon {
    String name, type1, type2, ability, nature, item;
    int hp, atk, def, spa, spd, spe;

    public Pokemon(String name, String type1, String type2, String ability, String nature, String item,
                   int hp, int atk, int def, int spa, int spd, int spe) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.ability = ability;
        this.nature = nature;
        this.item = item;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spa = spa;
        this.spd = spd;
        this.spe = spe;
    }

    public int totalEVs() {
        return hp + atk + def + spa + spd + spe;
    }

    public String toString() {
        return name + " (" + type1 + "/" + type2 + ") | Ability=" + ability + " | Item=" + item + " | Nature=" + nature + " | EVs: " +
                "HP=" + hp + " ATK=" + atk + " DEF=" + def +
                " SPA=" + spa + " SPD=" + spd + " SPE=" + spe;
    }
}

class Team {
    String name;
    List<Pokemon> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}

public class PokemonDB {

    static final String POKEMON_DATA_FILE = "pokemon.txt";
    static final String TEAM_FOLDER = "teams";

    static final Set<String> TYPE1_ALLOWED = Set.of(
            "normal", "fire", "water", "grass", "flying", "fighting", "poison",
            "electric", "ground", "rock", "psychic", "ice", "bug", "ghost",
            "steel", "dragon", "dark", "fairy"
    );

    static final Set<String> TYPE2_ALLOWED = Set.of(
            "n/a", "normal", "fire", "water", "grass", "flying", "fighting", "poison",
            "electric", "ground", "rock", "psychic", "ice", "bug", "ghost",
            "steel", "dragon", "dark", "fairy"
    );

    static List<Pokemon> pokemonList = new ArrayList<>();
    static List<Team> teams = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        loadPokemonData();
        loadTeams();

        while (true) {
            System.out.println("\n1. Add Pokemon");
            System.out.println("2. Search Pokemon");
            System.out.println("3. Build Team");
            System.out.println("4. List Pokemon");
            System.out.println("5. List Teams");
            System.out.println("6. Delete Pokemon");
            System.out.println("7. Delete Team");
            System.out.println("8. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1 -> addPokemon(sc);
                case 2 -> searchPokemon(sc);
                case 3 -> buildTeam(sc);
                case 4 -> listPokemon();
                case 5 -> listTeams();
                case 6 -> deletePokemon(sc);
                case 7 -> deleteTeam(sc);
                case 8 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------------- EV CHECK ----------------
    static boolean validEVs(int hp, int atk, int def, int spa, int spd, int spe) {
        return (hp + atk + def + spa + spd + spe) <= 66;
    }

    static boolean isValidType1(String type) {
        return type != null && TYPE1_ALLOWED.contains(type.trim().toLowerCase());
    }

    static boolean isValidType2(String type) {
        return type != null && TYPE2_ALLOWED.contains(type.trim().toLowerCase());
    }

    // ---------------- ADD ----------------
    static void addPokemon(Scanner sc) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        for (Pokemon p : pokemonList) {
            if (p.name.equalsIgnoreCase(name)) {
                System.out.println("Pokemon already exists!");
                return;
            }
        }

        System.out.print("Ability: ");
        String ability = sc.nextLine().trim();

        System.out.print("Item: ");
        String item = sc.nextLine().trim();

        System.out.print("Type1: ");
        String type1 = sc.nextLine().trim();
        if (!isValidType1(type1)) {
            System.out.println("Invalid Type1. Allowed types: Normal, Fire, Water, Grass, Flying, Fighting, Poison, Electric, Ground, Rock, Psychic, Ice, Bug, Ghost, Steel, Dragon, Dark, Fairy");
            return;
        }

        System.out.print("Type2: ");
        String type2 = sc.nextLine().trim();
        if (!isValidType2(type2)) {
            System.out.println("Invalid Type2. Allowed types: N/A, Normal, Fire, Water, Grass, Flying, Fighting, Poison, Electric, Ground, Rock, Psychic, Ice, Bug, Ghost, Steel, Dragon, Dark, Fairy");
            return;
        }

        System.out.print("Nature: ");
        String nature = sc.nextLine().trim();

        System.out.println("Enter EVs (total <= 66)");
        System.out.print("HP: ");
        int hp = sc.nextInt();
        System.out.print("ATK: ");
        int atk = sc.nextInt();
        System.out.print("DEF: ");
        int def = sc.nextInt();
        System.out.print("SPA: ");
        int spa = sc.nextInt();
        System.out.print("SPD: ");
        int spd = sc.nextInt();
        System.out.print("SPE: ");
        int spe = sc.nextInt();
        sc.nextLine();

        if (!validEVs(hp, atk, def, spa, spd, spe)) {
            System.out.println("Invalid EV total!");
            return;
        }

        pokemonList.add(new Pokemon(name, type1, type2, ability, nature, item, hp, atk, def, spa, spd, spe));
        savePokemonData();
        System.out.println("Pokemon added and saved.");
    }

    // ---------------- SEARCH ----------------
    static void searchPokemon(Scanner sc) {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokemon available.");
            return;
        }

        System.out.print("Search by name: ");
        String query = sc.nextLine().trim();

        for (Pokemon p : pokemonList) {
            if (p.name.equalsIgnoreCase(query)) {
                System.out.println(p);
                return;
            }
        }

        System.out.println("Pokemon not found.");
    }

    static void listPokemon() {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokemon available.");
            return;
        }

        System.out.println("Available Pokemon:");
        for (int i = 0; i < pokemonList.size(); i++) {
            Pokemon p = pokemonList.get(i);
            System.out.println((i + 1) + ". " + p.name + " - " + p.type1 + "/" + p.type2);
        }
    }

    static void listTeams() {
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
            return;
        }

        System.out.println("Saved Teams:");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.println((i + 1) + ". " + team.name);
            for (Pokemon member : team.members) {
                System.out.println("   - " + member.name);
            }
        }
    }

    static void deletePokemon(Scanner sc) {
        if (pokemonList.isEmpty()) {
            System.out.println("No Pokemon to delete.");
            return;
        }

        listPokemon();
        System.out.print("Select Pokemon number to delete: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > pokemonList.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Pokemon removed = pokemonList.remove(choice - 1);
        savePokemonData();
        deleteTeamsWithPokemon(removed.name);
        System.out.println("Deleted Pokemon: " + removed.name);
    }

    static void deleteTeam(Scanner sc) {
        if (teams.isEmpty()) {
            System.out.println("No teams to delete.");
            return;
        }

        listTeams();
        System.out.print("Select team number to delete: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice < 1 || choice > teams.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Team removed = teams.remove(choice - 1);
        deleteTeamFile(removed);
        System.out.println("Deleted team: " + removed.name);
    }

    static void deleteTeamsWithPokemon(String pokemonName) {
        List<Team> removedTeams = new ArrayList<>();
        for (Team team : teams) {
            for (Pokemon member : team.members) {
                if (member.name.equalsIgnoreCase(pokemonName)) {
                    removedTeams.add(team);
                    break;
                }
            }
        }

        for (Team team : removedTeams) {
            teams.remove(team);
            deleteTeamFile(team);
            System.out.println("Deleted team because it contained " + pokemonName + ": " + team.name);
        }
    }

    // ---------------- TEAM BUILDER ----------------
    static void buildTeam(Scanner sc) {
        if (pokemonList.size() < 6) {
            System.out.println("Need at least 6 Pokemon!");
            return;
        }

        List<Pokemon> teamMembers = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            System.out.println("\nChoose Pokemon " + (i + 1));
            for (int j = 0; j < pokemonList.size(); j++) {
                System.out.println((j + 1) + ". " + pokemonList.get(j).name);
            }

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice < 1 || choice > pokemonList.size()) {
                System.out.println("Invalid selection.");
                i--;
                continue;
            }

            Pokemon selected = pokemonList.get(choice - 1);
            if (teamMembers.contains(selected)) {
                System.out.println("No duplicates allowed!");
                i--;
                continue;
            }
            teamMembers.add(selected);
        }

        System.out.print("Team name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Team name cannot be empty.");
            return;
        }

        Team team = new Team(name);
        team.members.addAll(teamMembers);
        teams.add(team);
        saveTeamToFile(team);
        System.out.println("Team saved!");
    }

    // ---------------- SAVE / LOAD ----------------
    static void savePokemonData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(POKEMON_DATA_FILE))) {
            for (Pokemon p : pokemonList) {
                pw.println(escape(p.name) + "|" + escape(p.type1) + "|" + escape(p.type2) + "|" +
                        escape(p.ability) + "|" + escape(p.nature) + "|" + escape(p.item) + "|" +
                        p.hp + "|" + p.atk + "|" + p.def + "|" + p.spa + "|" + p.spd + "|" + p.spe);
            }
        } catch (IOException e) {
            System.out.println("Error saving pokemon data.");
        }
    }

    static void loadPokemonData() {
        File file = new File(POKEMON_DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length != 12) continue;
                String name = unescape(parts[0]);
                String type1 = unescape(parts[1]);
                String type2 = unescape(parts[2]);
                String ability = unescape(parts[3]);
                String nature = unescape(parts[4]);
                String item = unescape(parts[5]);
                int hp = Integer.parseInt(parts[6]);
                int atk = Integer.parseInt(parts[7]);
                int def = Integer.parseInt(parts[8]);
                int spa = Integer.parseInt(parts[9]);
                int spd = Integer.parseInt(parts[10]);
                int spe = Integer.parseInt(parts[11]);
                pokemonList.add(new Pokemon(name, type1, type2, ability, nature, item, hp, atk, def, spa, spd, spe));
            }
        } catch (IOException e) {
            System.out.println("Error loading pokemon data.");
        }
    }

    static void loadTeams() {
        File folder = new File(TEAM_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) return;

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String nameLine = br.readLine();
                if (nameLine == null || !nameLine.startsWith("Team: ")) continue;
                String teamName = nameLine.substring(6).trim();
                Team team = new Team(teamName);
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) continue;
                    String memberName = line.trim();
                    Pokemon found = findPokemonByName(memberName);
                    if (found != null) {
                        team.members.add(found);
                    } else {
                        team.members.add(new Pokemon(memberName, "Unknown", "Unknown", "Unknown", "Unknown", "Unknown", 0, 0, 0, 0, 0, 0));
                    }
                }
                teams.add(team);
            } catch (IOException e) {
                System.out.println("Error loading team file: " + file.getName());
            }
        }
    }

    static void saveTeamToFile(Team team) {
        try {
            File folder = new File(TEAM_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder, sanitizeFileName(team.name) + ".txt");
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.println("Team: " + team.name);
                for (Pokemon p : team.members) {
                    pw.println(p.name);
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving team.");
        }
    }

    static void deleteTeamFile(Team team) {
        File file = new File(TEAM_FOLDER, sanitizeFileName(team.name) + ".txt");
        if (file.exists()) {
            file.delete();
        }
    }

    static Pokemon findPokemonByName(String name) {
        for (Pokemon p : pokemonList) {
            if (p.name.equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    static String sanitizeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    static String escape(String value) {
        return value.replace("|", "\\|");
    }

    static String unescape(String value) {
        return value.replace("\\|", "|");
    }
}
