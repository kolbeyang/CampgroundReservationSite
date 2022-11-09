export class Reservation {
    id: number;
    campsiteId: number;
    startDate: number; //TODO
    endDate: number;
    username: string;
    paid: boolean;
    price: number;

    constructor(id: number, campsiteId: number, startDate: number, endDate: number, username: string, paid: boolean, price: number) {
        this.id = id;
        this.campsiteId = campsiteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.username = username;
        this.paid = paid;
        this.price = price;

    }
}