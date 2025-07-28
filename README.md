# 📅 Meeting Scheduler - Time Grains

Este projeto é um solucionador automático de agendamento de reuniões usando **OptaPlanner**. A aplicação lê uma planilha Excel com dados de reuniões, processa os dados de acordo com restrições e preferências, e gera uma nova planilha com o agendamento resolvido.

---

## 📁 Estrutura do Projeto

```
Meeting-scheduler/
│
├── .gitignore
├── README.md
└── MeetingSchedulerimeGrains/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/mycompany/app/SchedulerApp.java
        │   └── resources/
        │       └── data/
        │           ├── unsolved/
        │           │   └── sampledata2.xlsx         ← Planilha de entrada (a ser resolvida)
        │           └── solved/
        │               └── sample2solved.xlsx        ← Planilha de saída (resolvida)
```

---

## 🚀 Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/MeetingSchedulerTimeGrains.git
cd Meeting-Scheduler/MettingSchedulerTimeGrains
```

### 2. Compile o projeto com Maven

```bash
mvn clean install
```

---

## 🛠️ Configuração do SchedulerApp

A entrada e saída da aplicação são arquivos `.xlsx` localizados em:

- 📥 **Arquivo de entrada (não resolvido)**:  
  `src/main/resources/data/unsolved/sampledata2.xlsx`

- 📤 **Arquivo de saída (resolvido)**:  
  `src/main/resources/data/solved/sample2solved.xlsx`

### ✏️ Você pode alterar esses caminhos diretamente no arquivo `SchedulerApp.java`:

```java
public class SchedulerApp {
    public static void main(String[] args) {
        String sourcePath = "src\\main\\resources\\data\\unsolved\\sampledata2.xlsx";
        String destinationPath = "src\\main\\resources\\data\\solved\\";
        
        SchedulerSolution problem = getSolutionTable(sourcePath);
        Solver<SchedulerSolution> solver = getSolver();
        SchedulerSolution solution = solver.solve(problem);
        
        printSolution(solution);
        showResults(solution);
        checkResult(solution);
        
        List<Topic> topicList = solution.getTopicList();
        ExcelWriter.writeTopicsToExcel(topicList, destinationPath + "sample2solved.xlsx");
    }
}
```

---

## ✅ O que o sistema faz

- Lê uma planilha com os dados de tópicos, horários, salas e participantes.
- Considera restrições como capacidade de sala, sobreposição de horários e preferências.
- Resolve o problema usando o motor de otimização do OptaPlanner.
- Exporta a solução como uma nova planilha `.xlsx`, com os horários definidos, dias, salas e nomes dos tópicos organizados.

---

## 📊 Formato da planilha de saída

A planilha gerada (`sample2solved.xlsx`) contém as seguintes colunas:

| Nome do Tópico | Duração (min) | Dia         | Sala     | Início  |
|----------------|---------------|-------------|----------|---------|
| Reunião A      | 60            | 2025-08-12  | Sala A   | 09:00   |
| Reunião B      | 45            | 2025-08-12  | Sala B   | 10:00   |

O horário de início é formatado automaticamente em `HH:mm` com ajuste para horários após o meio-dia.

---

## 🤝 Requisitos

- Java 21+
- Maven 3.6+
- Planilha Excel no formato esperado (ver exemplos em `unsolved/`)

---

## 📫 Contribuição

Sinta-se livre para abrir issues ou pull requests para melhorar o projeto!

---

## 🧠 Autor

Desenvolvido por **Mateus Tavares**  
📧 mateus.costa.beltra@gmail.com

---