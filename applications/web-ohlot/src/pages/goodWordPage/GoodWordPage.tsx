import React, {useEffect, useState} from 'react'
import {IGoodWord, IGetGoodWordsFunction, GetGoodWordsFunction} from 'api/good-words'

export interface GoodWordPageProps {
    getGoodWordsFunction?: IGetGoodWordsFunction
}

const GoodWordPage = ({getGoodWordsFunction = GetGoodWordsFunction}: GoodWordPageProps) => {
    const [content, setContent] = useState<IGoodWord[]>([])
    useEffect(() => {
        getGoodWordsFunction().then(value => {
            setContent(value)
        })
    }, [])
    return (
        <div>
            {content.map(value => <div key={value.id}>{value.content}</div>)}
        </div>
    )
}
export default GoodWordPage