import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraFisica extends JFrame {
    private JTextField txtVelocidade, txtAltura, txtGravidade, txtAngulo;
    private JLabel lblResultadoTempo, lblResultadoVelocidade;

    public CalculadoraFisica() {
        setTitle("Calculadora de Queda Vertical e Lançamento");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1, 5, 5));

        // Componentes da Interface (Baseado no PDF)
        add(new JLabel("Velocidade Inicial (m/s):"));
        txtVelocidade = new JTextField();
        add(txtVelocidade);

        add(new JLabel("Altura (m):"));
        txtAltura = new JTextField();
        add(txtAltura);

        add(new JLabel("Gravidade (m/s²):"));
        txtGravidade = new JTextField("9.8");
        add(txtGravidade);

        add(new JLabel("Ângulo (0 a -90°):"));
        txtAngulo = new JTextField();
        add(txtAngulo);

        JButton btnCalcular = new JButton("Calcular");
        JButton btnReset = new JButton("Reset");
        add(btnCalcular);
        add(btnReset);

        lblResultadoTempo = new JLabel("Tempo total: -- s");
        lblResultadoVelocidade = new JLabel("Velocidade de impacto: -- m/s");
        add(lblResultadoTempo);
        add(lblResultadoVelocidade);

        // Ação do botão Calcular
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });

        // Ação do botão Reset
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
    }

    // Calcular o tempo e a velocidade final com base no ângulo e altura
    private void calcular() {
        try {
            double v0 = Double.parseDouble(txtVelocidade.getText());
            double h = Double.parseDouble(txtAltura.getText());
            double g = Double.parseDouble(txtGravidade.getText());
            double anguloGraus = Double.parseDouble(txtAngulo.getText());

            // Validação de limites (Baseado no PDF)
            if (h <= 0) throw new Exception("A altura deve ser positiva.");
            if (anguloGraus > 0 || anguloGraus < -90) throw new Exception("Ângulo entre 0 e -90.");

            // Converter ângulo para radianos
            double anguloRad = Math.toRadians(anguloGraus);

            // Calcular componentes da velocidade inicial
            // v0y é positiva para baixo se considerarmos g positivo e h positivo
            double v0y = -v0 * Math.sin(anguloRad); 
            double v0x = v0 * Math.cos(anguloRad);

            // Calcular Tempo usando Bhaskara: h = v0y*t + 0.5*g*t^2 -> 0.5*g*t^2 + v0y*t - h = 0
            double a = 0.5 * g;
            double b = v0y;
            double c = -h;

            double delta = (b * b) - (4 * a * c);
            if (delta < 0) throw new Exception("Cálculo impossível para estes dados.");

            double t = (-b + Math.sqrt(delta)) / (2 * a);

            // Calcular Velocidade Final (Impacto)
            double vy = v0y + g * t;
            double vFinal = Math.sqrt(Math.pow(v0x, 2) + Math.pow(vy, 2));
            double vKmH = vFinal * 3.6;

            // Mostrar resultados formatados
            lblResultadoTempo.setText(String.format("Tempo total: %.2f s", t));
            lblResultadoVelocidade.setText(String.format("Velocidade: %.2f m/s (%.2f km/h)", vFinal, vKmH));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Insira apenas números válidos.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Limpar todos os campos e resultados
    private void limparCampos() {
        txtVelocidade.setText("");
        txtAltura.setText("");
        txtAngulo.setText("");
        txtGravidade.setText("9.8");
        lblResultadoTempo.setText("Tempo total: -- s");
        lblResultadoVelocidade.setText("Velocidade de impacto: -- m/s");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalculadoraFisica().setVisible(true);
        });
    }
}