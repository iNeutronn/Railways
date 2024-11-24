export class Ticket {
  ticketId: number;
  clientId: number;
  price: number;

  constructor(
    ticketId: number,
    clientId: number,
    price: number
  ) {
    this.ticketId = ticketId;
    this.clientId = clientId;
    this.price = price;
  }
}
