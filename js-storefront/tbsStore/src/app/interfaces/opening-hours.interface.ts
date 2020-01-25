export interface OpenigHours {
  openingTime: {
    formattedHour?: string;
    hour: number;
    minute: number;
  };
  closingTime?: {
    formattedHour: string;
    hour: number;
    minute: number;
  };
  closed?: boolean;
  weekDay?: string;
}
