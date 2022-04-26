import {render, screen, waitFor} from '@testing-library/react'
import {IGoodWord} from "../../api/good-words";
import GoodWordPage from "./GoodWordPage";

/**
 * 공식 사이트 https://testing-library.com/docs/react-testing-library/intro/
 */

async function SpyGetGoodWordFunction(): Promise<IGoodWord[]> {
    return [{
        id: "id",
        content: "content",
        createAt: "2022-02-02 22:22:22",
        updateAt: "2022-02-02 22:22:22"
    }];
}

describe('GoodWordPage Test', () => {
    it('Call GetGoodWordFunction',   async () => {
        render(<GoodWordPage getGoodWordsFunction={SpyGetGoodWordFunction}/>)
        await waitFor(() => screen.getByText('content'))
    })
})