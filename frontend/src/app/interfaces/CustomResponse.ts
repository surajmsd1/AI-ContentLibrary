import { ContentPage } from "./ContentPage";

export interface CustomResponse {
    timeStamp: Date,
    statusCode: number,
    status: string,
    reason: string,
    message: string,
    developerMessage: string,
    data: {ContentPage?: ContentPage  , ContentPages?: ContentPage[]}
}
