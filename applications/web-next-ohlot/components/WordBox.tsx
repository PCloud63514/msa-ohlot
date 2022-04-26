import {useDrag} from "react-dnd";

type WordBoxProps = {
    message: string
}

export default function WordBox(props: WordBoxProps) {
    const [{opacity}, dragRef, previewRef]: any = useDrag(():any => ({
        type: 'wordBox',
        item: props.message,
        collect: (monitor:any) => ({
            opacity: monitor.isDragging() ? 0.5 : 1
        })
    }))
    return (
        <div
            ref={dragRef}
            className={"container"}
            onClick={() => {
            console.log(props.message)

        }}>
            <div className={"ui-text"}>
                {props.message}
            </div>
            <style jsx>
                {
                    `
                        .container {
                            opacity: ${opacity};
                            cursor: move;
                            margin 0 auto;
                            background-color: pink;
                            border: 1px solid black;
                            padding: 20px;
                            width: auto;
                            display: table;
                            border-radius: 8px;
                        }
                        
                        .ui-text {
                            text-align:center;
                            display: table-row;
                        }
                    `
                }
            </style>
        </div>
    )
}