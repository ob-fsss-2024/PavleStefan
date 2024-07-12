import { Component, inject, OnInit } from "@angular/core";
import { CountryData } from "../../types/countryData";
import { HttpClient } from "@angular/common/http";
import { NOTES_BASE_URL } from "../../app.config";

@Component({
    templateUrl: './home.view.html',
    styleUrls: ['./home.view.less'],
})
export class HomeView implements OnInit {
    private http = inject(HttpClient);

    name: string = '';
    countryData: CountryData;

    ngOnInit(): void {
        
        // Initialization logic if needed
    }

    nameChanged(name: string): void {
        this.name = name;
    }

    fetchResults(): void {
        this.http.get<CountryData>(`${NOTES_BASE_URL}/home/name?name=${this.name}`)
            .subscribe(countryData => this.countryData = countryData);
    }
}

