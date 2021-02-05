import {WeatherForecastModel} from './WeatherForecastModel';

export interface ForecastModel {
  id: number;
  weatherForecast: WeatherForecastModel;
  localization: string;
  forecastDate: string;
  forecastAcquiredDate: string;
}
