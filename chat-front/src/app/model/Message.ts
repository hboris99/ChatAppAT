import { User } from "./User";

export class Message{
  constructor(
    public recipients: User[] = [],
    public sender: string = '',
    public date: Date = new Date(),
    public subject: string = '',
    public content: string = ''

  )
  {}
}
