import { useEffect, useRef, useState } from "react"
import client from "react-stomp";
import SockJsClient from 'react-stomp'
/**
 *  Source này viết trên bootstrap 5, copy cũng 1 mớ style về, có thể dư vài trường như for, class, ... -> nhớ không cần quan tâm ba cái design làm chi
 */

export default (props) => {
    const [username, setUsername] = useState('');
    const [isRegistered, setIsRegistered] = useState(false);
    const [listTopics, setListTopics] = useState([]);
    var clientRef = useRef();

    const [listMessage, setListMessage] = useState([]);

    const getRandomHash = () => {
        return Math.random().toString();
    }

    const onMessageReceived = (message) => {
        const data = message
        addMessage({
            sender: data.sender,
            message: data.message,
            receiver: data.receiver,
            randomHash: data.randomHash
        })
    }

    const onRegisterUsername = () => {
        setListTopics([
            '/secured/user/queue/specific-user-' + username
        ])
        setIsRegistered(true);
    }

    const addMessage = (message) => {
        message = {...message, key: Math.random()}
        if (listMessage.some(u => u.randomHash == message.randomHash)) {
            return;
        }
        setListMessage([...listMessage, message]);
    }

    const onSendMessage = (e) => {
        if (e.key === 'Enter') {
            const tokens = e.target.value.split(':', 2).map(u => u.trim());
            const [receiver, message] = tokens;

            if (receiver == null || message == null) {
                alert('error!');
                return;
            }

            const payload = {
                sender: username,
                receiver: receiver,
                message: message,
                type: 'MESSAGE',
                randomHash: getRandomHash()
            };

            addMessage(payload);
            clientRef.current.client.send('/secured/room', {}, JSON.stringify(payload));
            
            e.target.value = '';
        }
    }

    return (
        <div class="container">
            <SockJsClient
                headers={{
                    username: username
                }}
                url='http://localhost:8080/secured/room'
                topics={listTopics}
                onMessage={onMessageReceived}
                ref={clientRef}
            />

            Current login user: <span style={{color: 'red'}}>{username}</span>

            <div class="card mt-3">
                <div class="card-header">
                    Đăng nhập
                </div>
                <div
                    class="card-body"
                >
                    <form onSubmit={(e) => e.preventDefault()}>
                        <div class="mb-3">
                            <label for="exampleInputEmail1" class="form-label">Username</label>
                            <input
                                type="text"
                                class={
                                    `form-control `
                                }
                                onChange={(e) => {
                                    setUsername(e.target.value);
                                }}
                            >

                            </input>
                            <div id="emailHelp" class="form-text">We'll never share your email with anyone else.</div>
                        </div>
                    </form>
                    <button
                        href="#"
                        class={`btn btn-primary ` + (isRegistered ? 'disabled' : '')}
                        onClick={() => {
                            setIsRegistered(true);
                            onRegisterUsername();
                        }}
                    >
                        Đăng ký
                    </button>
                </div>
            </div>

            <div class="container border mt-2" style={{
                height: 200,
                overflow: 'scroll'
            }}>
                {listMessage.map(message => {
                    return (
                        <p>
                            {message.sender == username ?
                                (
                                    <>
                                        <span style={{color: 'red', fontWeight: 'bold'}}>{'Gửi cho ' + message.receiver} </span>: {message.message}
                                    </>
                                )
                                :
                                (
                                    <>
                                        <span style={{color: 'green', fontWeight: 'bold'}}>{'Nhận từ ' + message.sender} </span>: {message.message}
                                    </>
                                )
                            }
                        </p>
                    )
                })}
            </div>
            <div class='mt-3'>
                <input
                    disabled={!isRegistered}
                    type='text'
                    class="form-control"
                    onKeyDown={onSendMessage}
                ></input>
            </div>
        </div>
    )
}
