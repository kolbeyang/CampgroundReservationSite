import { Product } from "./Product";

export class Campsite implements Product{
    name: string;
    id: number;
    rate: number;
    x: number;
    y: number;
    
    constructor(name: string, id: number, rate: number, x: number, y: number){
        this.name = name;
        this.id = id;
        this.rate = rate;
        this.x = x;
        this.y = y;
    }

}