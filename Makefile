JAVAC=javac
JAVA=java
JAR=jar
JAVAFX_LIB="javafx-sdk-22.0.1\lib"
CONTROLSFX_JAR="javafx-sdk-22.0.1\lib\controlsfx-11.2.1.jar"
JMETRO_JAR="javafx-sdk-22.0.1\lib\jmetro-11.6.16.jar"
MODULES=javafx.controls,javafx.fxml,org.controlsfx.controls

SRC=GUI/*.java MIPS/*.java
BIN_DIR=bin
JAR_FILE=simulador.jar
MAIN_CLASS=GUI.Main

# Compilação
comp:
	$(JAVAC) --module-path $(JAVAFX_LIB);$(CONTROLSFX_JAR) --add-modules $(MODULES) -cp $(JMETRO_JAR);. -d $(BIN_DIR) $(SRC)

# Execução
run:
	$(JAVA) --module-path $(JAVAFX_LIB);$(CONTROLSFX_JAR) --add-modules $(MODULES) -cp $(JMETRO_JAR);$(BIN_DIR) $(MAIN_CLASS)

# Criação do JAR executável
manifest:
	@echo "Main-Class: $(MAIN_CLASS)" > manifest.txt

jar: comp manifest
	$(JAR) cfm $(JAR_FILE) manifest.txt -C $(BIN_DIR) .
	$(JAR) uf $(JAR_FILE) -C $(JAVAFX_LIB) .
	$(JAR) uf $(JAR_FILE) -C $(CONTROLSFX_JAR) .
	$(JAR) uf $(JAR_FILE) -C $(JMETRO_JAR) .
	rm -f manifest.txt

# Execução do JAR
run-jar:
	$(JAVA) -jar $(JAR_FILE)

# Limpeza
clean:
	rm -rf $(BIN_DIR)/*.class $(JAR_FILE)