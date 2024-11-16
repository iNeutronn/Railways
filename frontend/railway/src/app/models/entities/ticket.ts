export class Ticket {
  ticketId: number;
  customerId: number;
  price: number;

  constructor(
    ticketId: number,
    customerId: number,
    price: number
  ) {
    this.ticketId = ticketId;
    this.customerId = customerId;
    this.price = price;
  }
}
