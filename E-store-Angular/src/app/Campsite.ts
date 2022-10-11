import { Product } from "./Product";

export class Campsite implements Product{
    name: string;
    id: number;
    rate: number;
    constructor(name: string, id: number, rate: number){
        this.name = name;
        this.id = id;
        this.rate = rate;
    }

}