export class LoginResponse {
    constructor(
        public username: string,
        public token: string,
        public isAdmin: boolean
    ) {}
}