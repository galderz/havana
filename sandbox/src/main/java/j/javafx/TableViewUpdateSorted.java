package j.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TableViewUpdateSorted extends Application {

   private TableView<StationBoardLine> table = new TableView<>();
   private final ExecutorService exec = Executors.newSingleThreadExecutor();

   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage stage) {
      BorderPane root = new BorderPane();
      Scene scene = new Scene(root, 800, 600);

      table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      table.setEditable(true);

      TableColumn typeCol = getTableCol("Type", 10, "type");
      TableColumn departureCol = getTableCol("Departure", 30, "departure");
      TableColumn stationCol = getTableCol("Station", 200, "station");
      TableColumn destinationCol = getTableCol("Destination", 200, "destination");
      TableColumn delayCol = getTableCol("Delay", 20, "delay");
      TableColumn trainName = getTableCol("Train Name", 50, "trainName");

      table.getColumns().addAll(
            typeCol, departureCol, stationCol, destinationCol, delayCol, trainName);

      root.setCenter(table);

      PartialResultsTask task = new PartialResultsTask();
      SortedList<StationBoardLine> sorted = new SortedList<>(task.getPartialResults(), new DelayComparator());
      table.setItems(sorted);
      exec.submit(task);

      stage.setTitle("Swiss Transport Delays Board");
      stage.setScene(scene);
      stage.show();
   }

   @Override
   public void stop() throws Exception {
      exec.shutdownNow();
   }

   private TableColumn getTableCol(String colName, int minWidth, String fieldName) {
      TableColumn<StationBoardLine, String> typeCol = new TableColumn<>(colName);
      typeCol.setMinWidth(minWidth);
      typeCol.setCellValueFactory(new PropertyValueFactory<>(fieldName));
      return typeCol;
   }

   static final class DelayComparator implements Comparator<StationBoardLine> {

      @Override
      public int compare(StationBoardLine o1, StationBoardLine o2) {
         return o1.getDelay().compareTo(o2.getDelay());
      }

   }

   public class PartialResultsTask extends Task<Void> {

      private ObservableList<StationBoardLine>partialResults = FXCollections.observableArrayList();

      public final ObservableList<StationBoardLine> getPartialResults() {
         return partialResults;
      }

      @Override protected Void call() throws Exception {
         System.out.println("Creating station board entries...");
         for (int i=5; i >= 1; i--) {
            Thread.sleep(1000);
            if (isCancelled()) break;
            StationBoardLine l = new StationBoardLine(
                  "ICE", "16:0" + i, "Basel Bad Bf", "Chur", String.valueOf(i), "ICE 75");
            Platform.runLater(() -> partialResults.add(l));
         }
         return null;
      }
   }

   public static final class StationBoardLine {
      private final SimpleStringProperty type;
      private final SimpleStringProperty departure;
      private final SimpleStringProperty station;
      private final SimpleStringProperty destination;
      private final SimpleStringProperty delay;
      private final SimpleStringProperty trainName;

      StationBoardLine(String type,
            String departure,
            String station,
            String destination,
            String delay,
            String trainName) {
         this.type = new SimpleStringProperty(type);
         this.departure = new SimpleStringProperty(departure);
         this.station = new SimpleStringProperty(station);
         this.destination = new SimpleStringProperty(destination);
         this.delay = new SimpleStringProperty(delay);
         this.trainName = new SimpleStringProperty(trainName);
      }

      public String getType() {
         return type.get();
      }

      public SimpleStringProperty typeProperty() {
         return type;
      }

      public void setType(String type) {
         this.type.set(type);
      }

      public String getDeparture() {
         return departure.get();
      }

      public SimpleStringProperty departureProperty() {
         return departure;
      }

      public void setDeparture(String departure) {
         this.departure.set(departure);
      }

      public String getStation() {
         return station.get();
      }

      public SimpleStringProperty stationProperty() {
         return station;
      }

      public void setStation(String station) {
         this.station.set(station);
      }

      public String getDestination() {
         return destination.get();
      }

      public SimpleStringProperty destinationProperty() {
         return destination;
      }

      public void setDestination(String destination) {
         this.destination.set(destination);
      }

      public String getDelay() {
         return delay.get();
      }

      public SimpleStringProperty delayProperty() {
         return delay;
      }

      public void setDelay(String delay) {
         this.delay.set(delay);
      }

      public String getTrainName() {
         return trainName.get();
      }

      public SimpleStringProperty trainNameProperty() {
         return trainName;
      }

      public void setTrainName(String trainName) {
         this.trainName.set(trainName);
      }
   }
}