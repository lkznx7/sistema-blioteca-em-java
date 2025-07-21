import java.util.*;
import java.time.LocalDate;

// Classe Livro
class Livro {
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponivel;

    public Livro(String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.disponivel = true;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public boolean isDisponivel() { return disponivel; }

    public void emprestar() { disponivel = false; }
    public void devolver() { disponivel = true; }

    @Override
    public String toString() {
        return titulo + " | " + autor + " | ISBN: " + isbn + " | " + (disponivel ? "Disponível" : "Emprestado");
    }
}

// Classe Usuario
class Usuario {
    private String nome;
    private String cpf;
    private List<Emprestimo> emprestimos;

    public Usuario(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
        this.emprestimos = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public List<Emprestimo> getEmprestimos() { return emprestimos; }

    public void adicionarEmprestimo(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
    }

    @Override
    public String toString() {
        return nome + " | CPF: " + cpf + " | Empréstimos: " + emprestimos.size();
    }
}

// Classe Emprestimo
class Emprestimo {
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    public Emprestimo(Livro livro) {
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = dataEmprestimo.plusDays(14); // prazo de 14 dias
        livro.emprestar();
    }

    public Livro getLivro() { return livro; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }

    @Override
    public String toString() {
        return livro.getTitulo() + " | De: " + dataEmprestimo + " Até: " + dataDevolucao;
    }
}

// Classe Principal
public class Main {
    private static List<Livro> livros = new ArrayList<>();
    private static List<Usuario> usuarios = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean executando = true;
        while (executando) {
            System.out.println("\n===== SISTEMA DA BIBLIOTECA =====");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("3. Listar Livros");
            System.out.println("4. Listar Usuários");
            System.out.println("5. Realizar Empréstimo");
            System.out.println("6. Devolver Livro");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> cadastrarUsuario();
                case 3 -> listarLivros();
                case 4 -> listarUsuarios();
                case 5 -> realizarEmprestimo();
                case 6 -> devolverLivro();
                case 7 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarLivro() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        livros.add(new Livro(titulo, autor, isbn));
        System.out.println("Livro cadastrado com sucesso!");
    }

    private static void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        usuarios.add(new Usuario(nome, cpf));
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void listarLivros() {
        System.out.println("\n--- Livros Cadastrados ---");
        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    private static void listarUsuarios() {
        System.out.println("\n--- Usuários Cadastrados ---");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    private static void realizarEmprestimo() {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        Usuario usuario = buscarUsuarioPorCpf(cpf);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        listarLivros();
        System.out.print("Digite o ISBN do livro para empréstimo: ");
        String isbn = scanner.nextLine();
        Livro livro = buscarLivroPorIsbn(isbn);
        if (livro == null || !livro.isDisponivel()) {
            System.out.println("Livro não disponível para empréstimo.");
            return;
        }

        Emprestimo emprestimo = new Emprestimo(livro);
        usuario.adicionarEmprestimo(emprestimo);
        System.out.println("Empréstimo realizado com sucesso!");
    }

    private static void devolverLivro() {
        System.out.print("CPF do usuário: ");
        String cpf = scanner.nextLine();
        Usuario usuario = buscarUsuarioPorCpf(cpf);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        List<Emprestimo> emprestimos = usuario.getEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Este usuário não possui empréstimos.");
            return;
        }

        System.out.println("Livros emprestados:");
        for (int i = 0; i < emprestimos.size(); i++) {
            System.out.println((i + 1) + ". " + emprestimos.get(i));
        }

        System.out.print("Escolha o número do empréstimo para devolver: ");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        if (escolha < 1 || escolha > emprestimos.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Emprestimo emprestimo = emprestimos.remove(escolha - 1);
        emprestimo.getLivro().devolver();
        System.out.println("Livro devolvido com sucesso.");
    }

    private static Usuario buscarUsuarioPorCpf(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf)) return u;
        }
        return null;
    }

    private static Livro buscarLivroPorIsbn(String isbn) {
        for (Livro l : livros) {
            if (l.getIsbn().equals(isbn)) return l;
        }
        return null;
    }
}
