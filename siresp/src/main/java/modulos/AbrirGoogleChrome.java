package modulos;


public class AbrirGoogleChrome {
	
	
    public void abrir() throws Exception {
        //ProcessBuilder pb = new ProcessBuilder("cmd.exe", "\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" --remote-debugging-port=9222 --user-data-dir=\"C:\\chrome-temp\"");
        
        ProcessBuilder pb = new ProcessBuilder("\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\"", "--remote-debugging-port=9222", "--user-data-dir=\"C:\\chrome-temp\"");

        pb.redirectErrorStream(true);   // junta stderr em stdout (facilita leitura)

        Process p = pb.start();

//        // Leitura assíncrona da saída para evitar deadlock
//        String output = readStream(p.getInputStream());
//        boolean finished = p.waitFor(15, TimeUnit.SECONDS); // timeout
//
//        if (!finished) {
//            p.destroyForcibly();
//            throw new RuntimeException("Comando não finalizou dentro do timeout.");
//        }
//
//        int exit = p.exitValue();
//        System.out.println("Exit code: " + exit);
//        System.out.println("Saída:\n" + output);
    }

//    static String readStream(InputStream is) throws IOException {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) sb.append(line).append(System.lineSeparator());
//            return sb.toString();
//        }
//    }
}
