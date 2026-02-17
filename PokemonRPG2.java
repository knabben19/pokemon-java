import java.util.*;

class Pokemon {
    String nome;
    String tipo;
    int level;
    int xp;
    int hp;
    int maxHp;
    String[] ataques;
    int[] danos;

    public Pokemon(String nome, String tipo, int level, int hp, String[] ataques, int[] danos) {
        this.nome = nome;
        this.tipo = tipo;
        this.level = level;
        this.hp = hp;
        this.maxHp = hp;
        this.xp = 0;
        this.ataques = ataques;
        this.danos = danos;
    }

    public boolean vivo() {
        return hp > 0;
    }

    public void curarTotal() {
        hp = maxHp;
    }

    public void ganharXP(int qtd) {
        xp += qtd;
        if (xp >= 50) {
            level++;
            xp = 0;
            maxHp += 20;
            hp = maxHp;
            System.out.println(nome + " subiu para level " + level + "!");
        }
    }

    public double vantagem(String tipoInimigo) {
        if (tipo.equals("Fogo") && tipoInimigo.equals("Planta")) return 2;
        if (tipo.equals("Planta") && tipoInimigo.equals("Fantasma")) return 2;
        if (tipo.equals("Fantasma") && tipoInimigo.equals("Fogo")) return 2;
        return 1;
    }

    public void atacar(Pokemon inimigo, int escolha) {
        double mult = vantagem(inimigo.tipo);
        int dano = (int)(danos[escolha] * mult);
        inimigo.hp -= dano;

        System.out.println(nome + " usou " + ataques[escolha]);
        if (mult > 1) System.out.println("Super efetivo!");
        System.out.println("Dano: " + dano);
    }
}

public class PokemonRPG2 {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();
    static ArrayList<Pokemon> time = new ArrayList<>();
    static HashMap<String, Integer> inventario = new HashMap<>();
    static int dinheiro = 100;

    static Pokemon pokemonSelvagem() {
        int r = rand.nextInt(3);

        if (r == 0)
            return new Pokemon("Bulbasaur", "Planta", 1, 80,
                    new String[]{"Vine Whip"}, new int[]{15});
        if (r == 1)
            return new Pokemon("Charmander", "Fogo", 1, 80,
                    new String[]{"Ember"}, new int[]{18});
        return new Pokemon("Gastly", "Fantasma", 1, 80,
                new String[]{"Lick"}, new int[]{17});
    }

    static void centroPokemon() {
        for (Pokemon p : time) {
            p.curarTotal();
        }
        System.out.println("Todos os Pokémon foram curados!");
    }

    static void loja() {
        System.out.println("Dinheiro: $" + dinheiro);
        System.out.println("1 - Poção (30 HP) - $20");

        int op = sc.nextInt();
        if (op == 1 && dinheiro >= 20) {
            inventario.put("Pocao", inventario.getOrDefault("Pocao", 0) + 1);
            dinheiro -= 20;
            System.out.println("Comprou Poção!");
        } else {
            System.out.println("Dinheiro insuficiente.");
        }
    }

    static void usarPocao(Pokemon p) {
        if (inventario.getOrDefault("Pocao", 0) > 0) {
            p.hp += 30;
            if (p.hp > p.maxHp) p.hp = p.maxHp;
            inventario.put("Pocao", inventario.get("Pocao") - 1);
            System.out.println("Poção usada!");
        } else {
            System.out.println("Sem poções.");
        }
    }

    static void batalha(Pokemon inimigo) {

        while (true) {

            System.out.println("\nEscolha Pokémon:");
            for (int i = 0; i < time.size(); i++) {
                Pokemon p = time.get(i);
                System.out.println(i + " - " + p.nome + " HP: " + p.hp);
            }

            int escolha = sc.nextInt();
            Pokemon ativo = time.get(escolha);

            if (!ativo.vivo()) {
                System.out.println("Desmaiado!");
                continue;
            }

            while (ativo.vivo() && inimigo.vivo()) {

                System.out.println("\n1-Atacar  2-Usar Poção");
                int op = sc.nextInt();

                if (op == 1) {
                    for (int i = 0; i < ativo.ataques.length; i++)
                        System.out.println(i + " - " + ativo.ataques[i]);
                    int atk = sc.nextInt();
                    ativo.atacar(inimigo, atk);
                }

                if (op == 2) {
                    usarPocao(ativo);
                }

                if (inimigo.vivo()) {
                    // IA inteligente: se tiver vantagem, ataca
                    double mult = inimigo.vantagem(ativo.tipo);
                    int atkIA = (mult > 1) ? 0 : rand.nextInt(inimigo.ataques.length);
                    inimigo.atacar(ativo, atkIA);
                }
            }

            if (!inimigo.vivo()) {
                System.out.println("Vitória!");
                ativo.ganharXP(25);
                dinheiro += 30;
                return;
            }

            boolean todosMortos = true;
            for (Pokemon p : time)
                if (p.vivo()) todosMortos = false;

            if (todosMortos) {
                System.out.println("Você perdeu...");
                return;
            }
        }
    }

    static void cidade() {
        while (true) {
            System.out.println("\n=== Cidade ===");
            System.out.println("1 - Centro Pokémon");
            System.out.println("2 - Rota (batalhar)");
            System.out.println("3 - Loja");
            System.out.println("4 - Voltar");

            int op = sc.nextInt();

            if (op == 1) centroPokemon();
            if (op == 2) batalha(pokemonSelvagem());
            if (op == 3) loja();
            if (op == 4) return;
        }
    }

    public static void main(String[] args) {

        Pokemon inicial = new Pokemon("Charmander", "Fogo", 1, 100,
                new String[]{"Ember"}, new int[]{18});

        time.add(inicial);

        inventario.put("Pocao", 1);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1 - Ir para Cidade");
            System.out.println("2 - Ver Time");
            System.out.println("3 - Ver Inventário");
            System.out.println("4 - Sair");

            int op = sc.nextInt();

            if (op == 1) cidade();

            if (op == 2) {
                for (Pokemon p : time)
                    System.out.println(p.nome + " Lv:" + p.level + " HP:" + p.hp);
            }

            if (op == 3) {
                System.out.println("Inventário: " + inventario);
                System.out.println("Dinheiro: $" + dinheiro);
            }

            if (op == 4) break;
        }
    }
}
