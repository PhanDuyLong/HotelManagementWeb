/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.dtos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CartDTO implements Serializable {

    private Map<Integer, TblRoomsDTO> cart;

    public CartDTO() {
    }

    public int amount() {
        if (cart == null) {
            return 0;
        }
        int totalAmount = 0;
        for (TblRoomsDTO room : cart.values()) {
            totalAmount += room.getAmount();
        }
        return totalAmount;
    }

    public CartDTO(Map<Integer, TblRoomsDTO> cart) {
        this.cart = cart;
    }

    public Map<Integer, TblRoomsDTO> getCart() {
        return cart;
    }

    public void setCart(Map<Integer, TblRoomsDTO> cart) {
        this.cart = cart;
    }

    public void add(TblRoomsDTO room) {
        if (cart == null) {
            cart = new HashMap();
        }
        if (this.cart.containsKey(room.getRoomID())) {
            int newAmount = cart.get(room.getRoomID()).getAmount() + room.getAmount();
            room.setAmount(newAmount);
        }
        cart.put(room.getRoomID(), room);
    }

    public void delete(int id) {
        if (cart == null) {
            return;
        }
        if (cart.containsKey(id)) {
            cart.remove(id);
            if (cart.isEmpty()) {
                cart = null;
            }
        }
    }

    public void update(int id, TblRoomsDTO product) {
        if (cart != null) {
            if (cart.containsKey(id)) {
                cart.replace(id, product);
            }
        }
    }

}
