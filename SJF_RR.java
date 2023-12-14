import java.util.*;

class Process {
  // Process properties
  int processId;
  int arrivalTime;
  int burstTime;
  int waitingTime;
  int turnaroundTime;
  int remainingTime;
  int responseTime;
}

public class SJF_RR {

  // Function to sort the process according to arrival time and burst time
  static void sortByArrivalTimeAndBurstTime(Process process[], int processCount) {
    for (int i = 0; i < processCount; i++) {
      for (int j = i + 1; j < processCount; j++) {
        // Sort by arrival time
        if (process[i].arrivalTime > process[j].arrivalTime) {
          // Swap processes
          Process temp = process[i];
          process[i] = process[j];
          process[j] = temp;
        } else if (process[i].arrivalTime == process[j].arrivalTime) {
          // If arrival times are equal, sort by burst time (SJF)
          if (process[i].burstTime > process[j].burstTime) {
            // Swap processes
            Process temp = process[i];
            process[i] = process[j];
            process[j] = temp;
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    int i, processCount = 0, time, remaining, quantum;
    boolean flag = false;

    Process process[] = new Process[100];
    float averageWaitingTime = 0, averageTurnaroundTime = 0, averageResponseTime = 0;

    System.out.println("Shortest Job with Round Robin Scheduling");
    System.out.println("Note - \n1. Arrival Time of at least one process should be 0");
    System.out.println("2. Can only handle a maximum of 100 processes");

    // Input process details until user enters 0 0 0
    while (true) {
      System.out.println("Enter process ID, arrival time, burst time (0 0 0 to stop): ");
      int processId = scanner.nextInt();
      int arrivalTime = scanner.nextInt();
      int burstTime = scanner.nextInt();

      if (processId == 0 && arrivalTime == 0 && burstTime == 0) {
        break;
      }

      process[processCount] = new Process();

      // Set process properties
      process[processCount].processId = processId;
      process[processCount].arrivalTime = arrivalTime;
      process[processCount].burstTime = burstTime;
      process[processCount].remainingTime = process[processCount].burstTime;
      process[processCount].responseTime = -1; // Initialize response time

      processCount++;
    }

    remaining = processCount;

    // Sort processes by arrival time and burst time
    sortByArrivalTimeAndBurstTime(process, processCount);

    // Enter quantum time
    System.out.print("Enter quantum number : ");
    quantum = scanner.nextInt();

    // Print Gantt chart header
    System.out.print("\n\nGantt Chart: ");
    System.out.print("0");

    // Execute process scheduling
    for (time = 0, i = 0; remaining != 0;) {
      // Check if the remaining time is less than or equal to the quantum
      if (process[i].remainingTime <= quantum && process[i].remainingTime > 0) {
        // If process first enters the CPU, record its response time
        if (process[i].responseTime == -1) {
          process[i].responseTime = time - process[i].arrivalTime;
        }

        // Update process properties
        time += process[i].remainingTime;
        process[i].remainingTime = 0;
        flag = true; // Process finished execution

        // Print process execution on Gantt chart
        System.out.print("-p" + process[i].processId + "-" + time);
      } else if (process[i].remainingTime > 0) {
        // Process has more time remaining, update and continue

        if (process[i].responseTime == -1) {
          process[i].responseTime = time - process[i].arrivalTime;
        }

        process[i].remainingTime -= quantum;
        time += quantum;

        // Print process execution on Gantt chart
System.out.print("-p" + process[i].processId + "-" + time);
      }

      // Check process completion and update turnaround and waiting times
      if (process[i].remainingTime == 0 && flag) {
        remaining--;

        process[i].turnaroundTime = time - process[i].arrivalTime;
        process[i].waitingTime = time - process[i].arrivalTime - process[i].burstTime;

        averageWaitingTime += time - process[i].arrivalTime - process[i].burstTime;
        averageTurnaroundTime += time - process[i].arrivalTime;

        flag = false; // Reset flag
      }

      // Update process execution index based on arrival time and remaining time
      if (i == processCount - 1) {
        i = 0; // If last process, start from first
      } else if (process[i + 1].arrivalTime <= time) {
        i++; // If next process arrived, move to next
      } else {
        i = 0; // If no process arrived, start from first again
      }
    }

    // Sort processes by process ID for display purposes
    Arrays.sort(process, 0, processCount, Comparator.comparing(p -> p.processId));

    StringBuilder sb = new StringBuilder();

    // Display process details and calculated times
    for (i = 0; i < processCount; i++) {
      sb.append("\n\nProcess id: ").append(process[i].processId)
          .append(",\tTurnaround Time: ").append(process[i].turnaroundTime)
          .append(",\tWaiting Time: ").append(process[i].waitingTime)
          .append(",\tResponse Time: ").append(process[i].responseTime);

      averageResponseTime += process[i].responseTime; //Sums all the process response times
    }

    System.out.println(sb.toString());

    // Calculate and display average times
    averageResponseTime = averageResponseTime / processCount;
    averageWaitingTime = averageWaitingTime / processCount;
    averageTurnaroundTime = averageTurnaroundTime / processCount;

    System.out.println("\n");
    System.out.println("Average Waiting Time : " + averageWaitingTime);
    System.out.println("Average Turnaround Time : " + averageTurnaroundTime);
    System.out.println("Average Response Time: " + averageResponseTime);
  }
}

