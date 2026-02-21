package com.bookrag;

import com.bookrag.core.config.ConfigLoader;
import com.bookrag.core.config.SystemConfig;
import com.bookrag.core.pipeline.IndexConstructor;
import com.bookrag.core.rag.BaseRag;
import com.bookrag.core.rag.RagFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.IFactory;

@SpringBootApplication
@Command(name = "bookrag",
        mixinStandardHelpOptions = true,
        description = "BookRAG: Hierarchical structure-aware RAG for complex documents",
        subcommands = {BookRagApplication.IndexCommand.class, BookRagApplication.RagCommand.class})
public class BookRagApplication implements CommandLineRunner, ExitCodeGenerator {

    private final IFactory factory;
    private int exitCode;

    public BookRagApplication(IFactory factory) {
        this.factory = factory;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(this, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(BookRagApplication.class, args)));
    }

    // ── Shared options inherited by subcommands ──

    @Option(names = {"-c", "--config"}, required = true,
            description = "Path to the main system configuration YAML file")
    String configPath;

    @Option(names = {"-d", "--dataset-config"},
            description = "Path to the dataset configuration YAML file")
    String datasetConfigPath;

    @Option(names = {"--debug"}, description = "Enable debug mode")
    boolean debug;

    @Option(names = {"--nsplit"}, defaultValue = "2",
            description = "Total number of splits for parallel processing")
    int nsplit;

    @Option(names = {"--num"}, defaultValue = "2",
            description = "Current split number (1-indexed)")
    int num;

    // ── Index subcommand ──

    @Command(name = "index", description = "Build search index from documents")
    static class IndexCommand implements Runnable {

        @CommandLine.ParentCommand
        BookRagApplication parent;

        @Option(names = {"--stage"}, defaultValue = "all",
                description = "Pipeline stage: tree, graph, vdb, all, rebuild_graph_vdb")
        String stage;

        @Override
        public void run() {
            System.out.println("=== BookRAG Index ===");
            System.out.println("Config: " + parent.configPath);
            System.out.println("Stage: " + stage);

            SystemConfig config = ConfigLoader.loadSystemConfig(parent.configPath);

            IndexConstructor constructor = new IndexConstructor();
            constructor.buildIndex(config, stage);

            System.out.println("=== Index Complete ===");
        }
    }

    // ── RAG subcommand ──

    @Command(name = "rag", description = "Run RAG inference using a pre-built index")
    static class RagCommand implements Runnable {

        @CommandLine.ParentCommand
        BookRagApplication parent;

        @Option(names = {"-q", "--query"},
                description = "A single query to run")
        String query;

        @Override
        public void run() {
            System.out.println("=== BookRAG Inference ===");
            System.out.println("Config: " + parent.configPath);

            SystemConfig config = ConfigLoader.loadSystemConfig(parent.configPath);

            if (query == null || query.isBlank()) {
                System.out.println("ERROR: RAG command requires a query (-q)");
                return;
            }

            System.out.println("Query: " + query);

            BaseRag agent = RagFactory.createRagAgent(config.getRag().getStrategyConfig());
            BaseRag.RagResult result = agent.generation(query, config.getSavePath());

            System.out.println("Answer: " + result.getAnswer());
            System.out.println("Retrieved nodes: " + result.getRetrievedNodeIds());

            agent.close();
            System.out.println("=== Inference Complete ===");
        }
    }
}
