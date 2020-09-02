//package pl.lodz.p.it.boorger.entities;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "reservation_menu_mapping", schema = "public", catalog = "d8vb0t26u3m4bd")
//public class ReservationMenuMapping {
//    private long id;
//    private long reservationId;
//    private long menuId;
//    private Reservation reservationByReservationId;
//    private Dish menuByMenuId;
//
//    @Id
//    @Column(name = "id")
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    @Basic
//    @Column(name = "reservation_id")
//    public long getReservationId() {
//        return reservationId;
//    }
//
//    public void setReservationId(long reservationId) {
//        this.reservationId = reservationId;
//    }
//
//    @Basic
//    @Column(name = "menu_id")
//    public long getMenuId() {
//        return menuId;
//    }
//
//    public void setMenuId(long menuId) {
//        this.menuId = menuId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ReservationMenuMapping that = (ReservationMenuMapping) o;
//
//        if (id != that.id) return false;
//        if (reservationId != that.reservationId) return false;
//        if (menuId != that.menuId) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (int) (reservationId ^ (reservationId >>> 32));
//        result = 31 * result + (int) (menuId ^ (menuId >>> 32));
//        return result;
//    }
//
//    @ManyToOne
//    @JoinColumn(name = "reservation_id", referencedColumnName = "id", nullable = false)
//    public Reservation getReservationByReservationId() {
//        return reservationByReservationId;
//    }
//
//    public void setReservationByReservationId(Reservation reservationByReservationId) {
//        this.reservationByReservationId = reservationByReservationId;
//    }
//
//    @ManyToOne
//    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
//    public Dish getMenuByMenuId() {
//        return menuByMenuId;
//    }
//
//    public void setMenuByMenuId(Dish menuByMenuId) {
//        this.menuByMenuId = menuByMenuId;
//    }
//}
