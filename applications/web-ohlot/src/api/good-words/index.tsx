import axios from "axios";

export interface IGoodWord {
    id: string,
    content: string,
    createAt: string | Date
    updateAt: string | Date
}

export type IGetGoodWordsFunction = () => Promise<IGoodWord[]>

export async function GetGoodWordsFunction():Promise<IGoodWord[]> {
    const response = await axios.get<IGoodWord[]>("http://localhost:8080/good-words")
    return response.data
}

