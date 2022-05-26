import { User } from "./User";

export class Message{
  constructor(
    public recipient: string = '',
    public sender: string = '',
    public date: Date = new Date(),
    public subject: string = '',
    public content: string = ''

  )
  {}
}
