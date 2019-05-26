package trial;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class FileUtility {

    static List<Path> getAllJavaFilePath(final Path rootDirectory) throws IOException {
        return Files.walk(rootDirectory)
                .filter(path -> path.toString().endsWith(".java"))
                .collect(Collectors.toList());
    }

}
