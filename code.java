import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends JFrame implements ActionListener {

    private JTextField cityTextField;
    private JLabel resultLabel;
    private JButton getWeatherButton;

    private static final String API_KEY = "b1fab2921c2a4edaa6f80559232412";
    private static final String API_URL = "https://api.weatherapi.com/v1/current.json?key=%s&q=%s";

    public WeatherForecast() {
        setTitle("Weather App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create UI components
        cityTextField = new JTextField(20);
        getWeatherButton = new JButton("Get Weather");
        resultLabel = new JLabel();

        // Add components to the frame
        add(new JLabel("City: "));
        add(cityTextField);
        add(getWeatherButton);
        add(resultLabel);

        setVisible(true);

        // Add action listener to the button
        getWeatherButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getWeatherButton) {
            try {
                getWeather();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching weather data.");
            }
        }
    }

    private void getWeather() throws IOException {
        String cityName = cityTextField.getText();
        String apiUrl = String.format(API_URL, API_KEY, cityName);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        String currentTemperature = parseApiResponse(response.toString(), "temp_c");

        resultLabel.setText("Current Temperature: " + currentTemperature + "°C");
    }

    private String parseApiResponse(String response, String key) {
        int index = response.indexOf("\"" + key + "\"");
        if (index != -1) {
            int startIndex = response.indexOf(":", index) + 1;
            int endIndex = response.indexOf(",", startIndex);
            return response.substring(startIndex, endIndex).trim();
        }
        return null;
    }

    public static void main(String[] args) {
        new WeatherForecast();
    }
}
