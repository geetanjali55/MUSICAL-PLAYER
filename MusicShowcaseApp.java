import java.util.*;
class Song {
    int id;
    String title, artist, genre;
    int duration;
    int playCount;

    public Song(int id, String title, String artist, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.playCount = 0;
    }

    @Override
    public String toString() {
        return id + ". " + title + " by " + artist + " [" + genre + "] - " + duration + "s | Played: " + playCount + " times";
    }
}

class User {
    String username;
    String password;
    List<Song> favorites = new ArrayList<>();
    Stack<Song> recentlyPlayed = new Stack<>();
    Queue<Song> playQueue = new LinkedList<>();
    Map<String, LinkedList<Song>> playlists = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class MusicShowcaseApp {
    static Scanner sc = new Scanner(System.in);
    static Map<String, String> userCredentials = new HashMap<>();
    static Map<String, User> users = new HashMap<>();
    static List<Song> songLibrary = new ArrayList<>();
    static int songIdCounter = 1;
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        preloadSongs();

        while (true) {
            System.out.println("\n=== Welcome to Music Showcase ===");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Browse Songs (Guest)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> signUp();
                case 3 -> browseSongs();
                case 4 -> {
                    System.out.println("Thank you for using Music Showcase!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void preloadSongs() {
        songLibrary.add(new Song(songIdCounter++, "Believer", "Imagine Dragons", "Rock", 204));
        songLibrary.add(new Song(songIdCounter++, "Shape of You", "Ed Sheeran", "Pop", 240));
        songLibrary.add(new Song(songIdCounter++, "Faded", "Alan Walker", "Electronic", 180));
        songLibrary.add(new Song(songIdCounter++, "Let Her Go", "Passenger", "Folk", 250));
        songLibrary.add(new Song(songIdCounter++, "Despacito", "Luis Fonsi", "Latin Pop", 230));
        songLibrary.add(new Song(songIdCounter++, "Blinding Lights", "The Weeknd", "R&B", 200));
        songLibrary.add(new Song(songIdCounter++, "God's Plan", "Drake", "Hip-Hop", 210));
        songLibrary.add(new Song(songIdCounter++, "Levitating", "Dua Lipa", "Pop", 220));
        songLibrary.add(new Song(songIdCounter++, "Smells Like Teen Spirit", "Nirvana", "Grunge", 300));
        songLibrary.add(new Song(songIdCounter++, "Bohemian Rhapsody", "Queen", "Classic Rock", 354));
    }

    static void signUp() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if (userCredentials.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        userCredentials.put(username, password);
        users.put(username, new User(username, password));
        System.out.println("User registered successfully!");
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            adminPanel();
        } else if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
            userMenu(users.get(username));
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    static void browseSongs(User user) {
        displaySongTable(songLibrary);
        
        System.out.print("Do you want to add a song to a playlist? (yes/no): ");
        String choice = sc.nextLine().toLowerCase();
        if (!choice.equals("yes")) return;
    
        System.out.print("Enter the Song ID to add: ");
        int songId = sc.nextInt(); sc.nextLine();
    
        Song selectedSong = null;
        for (Song song : songLibrary) {
            if (song.id == songId) {
                selectedSong = song;
                break;
            }
        }
    
        if (selectedSong == null) {
            System.out.println("Song not found.");
            return;
        }
    
        System.out.print("Do you want to add to an existing playlist or create a new one? (existing/new): ");
        String type = sc.nextLine().toLowerCase();
    
        if (type.equals("new")) {
            System.out.print("Enter new playlist name: ");
            String newName = sc.nextLine();
            if (user.playlists.containsKey(newName)) {
                System.out.println("Playlist already exists.");
                return;
            }
            LinkedList<Song> newList = new LinkedList<>();
            newList.add(selectedSong);
            user.playlists.put(newName, newList);
            System.out.println("Playlist '" + newName + "' created and song added.");
        } else if (type.equals("existing")) {
            if (user.playlists.isEmpty()) {
                System.out.println("No existing playlists. Please create one first.");
                return;
            }
            System.out.println("Available Playlists:");
            user.playlists.keySet().forEach(System.out::println);
    
            System.out.print("Enter the playlist name: ");
            String existingName = sc.nextLine();
            LinkedList<Song> existingList = user.playlists.get(existingName);
            if (existingList == null) {
                System.out.println("Playlist not found.");
            } else {
                existingList.add(selectedSong);
                System.out.println("Song added to playlist '" + existingName + "'.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    

    static void displaySongTable(List<Song> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
            return;
        }
    
        System.out.printf("%-5s %-30s %-20s %-15s %-12s %-12s%n",
                "ID", "Title", "Artist", "Genre", "Duration", "Play Count");
        System.out.println("----------------------------------------------------------------------------------------------");
    
        for (Song song : songs) {
            // Convert the song duration from seconds to minutes and seconds
            int minutes = song.duration / 60;
            int seconds = song.duration % 60;
    
            // Display the song info with duration in minutes and seconds
            System.out.printf("%-5d %-30s %-20s %-15s %2d:%02d     %-12d%n",
                    song.id, song.title, song.artist, song.genre, minutes, seconds, song.playCount);
        }
    }    
    

    static void adminPanel() {
        while (true) {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. Add Song");
            System.out.println("2. Remove Song");
            System.out.println("3. View All Songs");
            System.out.println("4. View Most Played Songs");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> addSong();
                case 2 -> removeSong();
                case 3 -> browseSongs();
                case 4 -> viewTopPlayed();
                case 5 -> {
                    System.out.println("Logging out from admin.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void browseSongs() {
        displaySongTable(songLibrary);
        System.out.print("Do you want to play a song? (yes/no): ");
        String response = sc.nextLine().toLowerCase();
        if (response.equals("yes")) {
            System.out.print("Enter song ID to play: ");
            int id = sc.nextInt(); sc.nextLine();
            for (Song song : songLibrary) {
                if (song.id == id) {
                    System.out.println("\nNow playing: " + song.title + " by " + song.artist);
                    song.playCount++;
                    return;
                }
            }
            System.out.println("Song not found.");
        }
    }
    

    static void addSong() {
        System.out.print("Enter song title: ");
        String title = sc.nextLine();
        System.out.print("Enter artist name: ");
        String artist = sc.nextLine();
        System.out.print("Enter genre: ");
        String genre = sc.nextLine();
        System.out.print("Enter duration (in seconds): ");
        int duration = sc.nextInt(); sc.nextLine();

        Song song = new Song(songIdCounter++, title, artist, genre, duration);
        songLibrary.add(song);
        System.out.println("Song added successfully.");
    }

    static void removeSong() {
        browseSongs();
        System.out.print("Enter song ID to remove: ");
        int id = sc.nextInt(); sc.nextLine();
        songLibrary.removeIf(song -> song.id == id);
        System.out.println("Song removed.");
    }

    static void viewTopPlayed() {
        List<Song> topSongs = songLibrary.stream()
                .sorted((a, b) -> b.playCount - a.playCount)
                .limit(5)
                .toList();
        displaySongTable(topSongs);
    }

    static void userMenu(User user) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. View All Songs");
            System.out.println("2. Search Songs");
            System.out.println("3. Play a Song");
            System.out.println("4. View Recently Played");
            System.out.println("5. Create Playlist");
            System.out.println("6. View Playlist");
            System.out.println("7. Add to Favorites");
            System.out.println("8. View Favorites");
            System.out.println("9. View Top Trending Songs");
            System.out.println("10. Play Songs by Genre");
            System.out.println("11. Logout");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> browseSongs(user);
                case 2 -> searchSongs();
                case 3 -> playSong(user);
                case 4 -> viewRecentlyPlayed(user);
                case 5 -> createPlaylist(user);
                case 6 -> viewPlaylist(user);
                case 7 -> addToFavorites(user);
                case 8 -> viewFavorites(user);
                case 9 -> viewTopPlayed();
                case 10 -> playSongsByGenre(user);
                case 11 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void searchSongs() {
        System.out.print("Enter keyword (title/artist/genre): ");
        String keyword = sc.nextLine().toLowerCase();
        List<Song> results = songLibrary.stream()
                .filter(song -> song.title.toLowerCase().contains(keyword) ||
                        song.artist.toLowerCase().contains(keyword) ||
                        song.genre.toLowerCase().contains(keyword))
                .toList();
        displaySongTable(results);
    }

    static void playSong(User user) {
        browseSongs();
        System.out.print("Enter song ID to play: ");
        int id = sc.nextInt(); sc.nextLine();
        for (Song song : songLibrary) {
            if (song.id == id) {
                System.out.println("\nPlaying: " + song.title);
                song.playCount++;
                user.recentlyPlayed.push(song);
                return;
            }
        }
        System.out.println("Song not found.");
    }

    static void playSongsByGenre(User user) {
        System.out.print("Enter genre to play: ");
        String genre = sc.nextLine().toLowerCase();
        List<Song> results = new ArrayList<>();
        for (Song song : songLibrary) {
            if (song.genre.toLowerCase().equals(genre)) {
                System.out.println("\nPlaying: " + song.title);
                song.playCount++;
                user.recentlyPlayed.push(song);
                results.add(song);
            }
        }
        if (results.isEmpty()) {
            System.out.println("No songs found for genre: " + genre);
        }
    }

    static void viewRecentlyPlayed(User user) {
        if (user.recentlyPlayed.isEmpty()) {
            System.out.println("No recently played songs.");
        } else {
            displaySongTable(user.recentlyPlayed);
        }
    }

    static void createPlaylist(User user) {
        System.out.print("Enter playlist name: ");
        String name = sc.nextLine();
        if (user.playlists.containsKey(name)) {
            System.out.println("Playlist already exists.");
            return;
        }
        LinkedList<Song> list = new LinkedList<>();
        System.out.println("Enter song IDs to add to playlist (comma separated): ");
        browseSongs();
        String[] ids = sc.nextLine().split(",");
        for (String s : ids) {
            try {
                int id = Integer.parseInt(s.trim());
                for (Song song : songLibrary) {
                    if (song.id == id) list.add(song);
                }
            } catch (Exception e) {
                System.out.println("Invalid ID: " + s);
            }
        }
        user.playlists.put(name, list);
        System.out.println("Playlist '" + name + "' created with " + list.size() + " songs.");
    }

    static void viewPlaylist(User user) {
        if (user.playlists.isEmpty()) {
            System.out.println("No playlists available.");
            return;
        }
        user.playlists.forEach((name, list) -> {
            System.out.println("\nPlaylist: " + name);
            displaySongTable(list);
        });
    }

    static void addToFavorites(User user) {
        browseSongs();
        System.out.print("Enter song ID to add to favorites: ");
        int id = sc.nextInt(); sc.nextLine();
        for (Song song : songLibrary) {
            if (song.id == id) {
                user.favorites.add(song);
                System.out.println("Added to favorites.");
                return;
            }
        }
        System.out.println("Song not found.");
    }

    static void viewFavorites(User user) {
        if (user.favorites.isEmpty()) System.out.println("No favorites added.");
        else displaySongTable(user.favorites);
    }
}