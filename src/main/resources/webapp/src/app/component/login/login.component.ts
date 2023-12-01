import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../service/auth.service';
import {Login} from '../../modal/login';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  savedUserEmail = localStorage.getItem('savedUserEmail') ?? '';

  loginForm: FormGroup<Login> = new FormGroup<Login>({
    email: new FormControl(this.savedUserEmail, {nonNullable: true, validators: [Validators.required, Validators.email]}),
    password: new FormControl('', {nonNullable: true, validators: Validators.required})
  });


  constructor(
    public authenticationService: AuthenticationService
  ) {
  }

  ngOnInit(): void {
    this.authenticationService.logout();
  }

  login(): void {
    if (this.loginForm.valid) {
      this.authenticationService.login(this.loginForm.getRawValue());
    }
  }
}
