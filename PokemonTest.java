public class PokemonTest {
    public static void main(String[] args) {
        // Create two Pokémon with different types
        Pokemon firePoke = new Pokemon("Charmander", 100, 20, "Fire", "Fire");
        Pokemon grassPoke = new Pokemon("Bulbasaur", 100, 20, "Grass", "Grass");

        // Display their initial info
        System.out.println("=== Initial Pokémon Stats ===");
        System.out.println(firePoke);
        System.out.println();
        System.out.println(grassPoke);
        System.out.println();

        // Fire-type attacks Grass-type (should be super effective)
        System.out.println("=== Battle Begins ===");
        firePoke.attack(grassPoke);
        System.out.println("After attack:");
        System.out.println(grassPoke);
        System.out.println("Is Bulbasaur defeated? " + grassPoke.isDefeated());
        System.out.println();

        // Grass-type attacks Fire-type (should be not very effective)
        grassPoke.attack(firePoke);
        System.out.println("After attack:");
        System.out.println(firePoke);
        System.out.println("Is Charmander defeated? " + firePoke.isDefeated());
    }
}
