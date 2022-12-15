import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Test {

    static class Order {
        private String id;
        private int available;


        public Order(String id, int available) {
            this.id = id;
            this.available = available;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getAvailable() {
            return available;
        }

        public void setAvailable(int available) {
            this.available = available;
        }
    }

    static class DesignPack {
        private List<String> orderIds = new ArrayList<>();
        private boolean isFull;
        private int capacity;
        private int maxCapacity = 14;

        void addOrder(Order order) {
            if (this.isOrderNotExistInDesignPack(order) && this.isDesignPackNotFull()) {
                this.orderIds.add(order.getId());
                this.capacity = this.capacity + order.getAvailable();
            }
        }

        boolean isOrderNotExistInDesignPack(Order order) {
            return !orderIds.contains(order.getId());
        }

        boolean isDesignPackNotFull() {
            if (this.capacity == this.maxCapacity) {
                this.isFull = true;
                return false;
            }
            return true;
        }

        public List<String> getOrderIds() {
            return orderIds;
        }

        public void setOrderIds(List<String> orderIds) {
            this.orderIds = orderIds;
        }

        public boolean isFull() {
            return isFull;
        }

        public void setFull(boolean full) {
            isFull = full;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getMaxCapacity() {
            return maxCapacity;
        }

        public void setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        List<Order> orders = List.of(new Order("1", 12), new Order("2", 11),
                new Order("3", 1), new Order("4", 3),
                new Order("5", 1), new Order("6", 7),
                new Order("7", 9), new Order("8", 1));

        System.out.println(new ObjectMapper().writeValueAsString(getDesignPack(orders, 14)));
        System.out.println(new ObjectMapper().writeValueAsString(getDesignPack(orders, 12)));
    }

    private static List<DesignPack> getDesignPack(List<Order> orders, final int maxCapacity) {
        List<DesignPack> designPacks = new ArrayList<>();
        for (Order order : orders) {

            if (order.getAvailable() > maxCapacity) continue;

            if (designPacks.isEmpty() || allDesignPacksFull(designPacks)) {
                DesignPack designPack = new DesignPack();
                designPack.addOrder(order);
                designPacks.add(designPack);
                continue;
            }
            DesignPack newDesignPack = new DesignPack();
            boolean isCreatedNewDesignPack = true;
            for (DesignPack designPack : designPacks) {
                if (designPack.isDesignPackNotFull()) {
                    if (designPack.isOrderNotExistInDesignPack(order) && (designPack.getCapacity() + order.getAvailable()) <= maxCapacity) {
                        designPack.addOrder(order);
                        isCreatedNewDesignPack = false;
                        break;
                    }
                }
            }
            if (isCreatedNewDesignPack) {
                newDesignPack.addOrder(order);
                designPacks.add(newDesignPack);
            }
            designPacks.sort(Comparator.comparing(DesignPack::getCapacity).reversed());
        }
        return designPacks;
    }

    private static boolean allDesignPacksFull(List<DesignPack> designPacks) {
        for (DesignPack designPack : designPacks) {
            if (designPack.isDesignPackNotFull()) {
                return false;
            }
        }
        return true;
    }
}
