package yatest;

import java.util.Locale;

/**
 * @author Constantine Mikhalev
 * 27/11/2021
 * YaTest
 */
public class DeliveryCalc {
    public static void main(String[] args) {
        Calc cl = null;
        try {
            cl = new DeliveryCalc().readArgs(args);
            Double  totalPrice = cl.calcDeliveryPrice();
            System.out.println(totalPrice);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();;
        }
    }

    private Calc readArgs(String[] args) throws Exception {
        Long distance = null;
        Double height = null, width = null, length = null;
        String workLoad = null;
        boolean fragility = false;

        for (String arg : args) {
            String[] pars = arg.split("=");
            switch (pars[0].trim().toLowerCase(Locale.ROOT)) {
                case "distance":
                    distance = Long.parseLong(pars[1]);
                    break;
                case "height":
                    height = Double.parseDouble(pars[1]);
                    break;
                case "width":
                    width = Double.parseDouble(pars[1]);
                    break;
                case "length":
                    length = Double.parseDouble(pars[1]);
                    break;
                case "fragility":
                    fragility = Boolean.parseBoolean(pars[1]);
                    break;
                case "workload":
                    workLoad = pars[1];
                    break;
            }
        }
        if(distance == null || distance < 1)
            throw new Exception("Error#1.1 Distance is not set");
        if(height == null || height < 0.1)
            throw new Exception("Error#1.2 Product height is not set");
        if(width == null || width < 0.1)
            throw new Exception("Error#1.3 Product width is not set");
        if(length == null || length <0.1)
            throw new Exception("Error#1.4 Product length is not set");
        return new Calc(distance, height, width, length, fragility, workLoad);
    }
}

enum WorkLoadType {High, Overload, Medium, Normal};

class Calc {
    private long distance;
    private double height;
    private double width;
    private double length;
    private boolean fragility;

    WorkLoadType workload;

    public Calc(long distance, double height, double width, double length, boolean fragility, String workload) {
        this.distance = distance;
        this.height = height;
        this.width = width;
        this.length = length;
        this.fragility = fragility;
        try {
            this.workload = WorkLoadType.valueOf(workload);
        } catch (IllegalArgumentException | NullPointerException e) {
            this.workload = null;
        }
    }


    public double calcDeliveryPrice() throws Exception {
        Double price = (calcDistance() + calcSize() + calcFragile()) * calcWorkLoad();
        if (price < 400)
            price = 400D;
        return price;
    }

    private double calcWorkLoad() {
        if (workload != null) {
            switch (workload) {
                case Overload:
                    return 1.6;
                case High:
                    return 1.4;
                case Medium:
                    return 1.2;
                default:
                    return 1;
            }
        }
        return 1;
    }

    private double calcDistance() {
        if (distance >= 30)
            return 300;
        else if (distance >= 10)
            return 200;
        else if (distance >= 2)
            return 100;
        return 50;
    }

    private double calcSize() {
        double size = height * width * length;
        if (size > 100)
            return 200;
        return 100;
    }

    private double calcFragile() throws Exception {
        if (!fragility)
            return 0;
        else if (fragility && distance > 30)
            throw new Exception("Error#5.1 You cannot request a delivery of a fragile product to distance more then 30 km");
        return 300;
    }

}
